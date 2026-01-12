package com.farkas.familymealmate.service.shoppinglist;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingItemDto;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListDto;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListUpdateRequest;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.model.enums.MealType;
import com.farkas.familymealmate.model.enums.Measurement;
import com.farkas.familymealmate.service.MealPlanService;
import com.farkas.familymealmate.service.ShoppingListService;
import com.farkas.familymealmate.testdata.mealplan.TestMealNotes;
import com.farkas.familymealmate.testdata.mealplan.TestMealPlanBuilder;
import com.farkas.familymealmate.testdata.recipe.TestRecipeFactory;
import com.farkas.familymealmate.testdata.recipe.TestRecipeIngredients;
import com.farkas.familymealmate.testdata.recipe.TestRecipes;
import com.farkas.familymealmate.testdata.shoppingList.TestFreeTextItems;
import com.farkas.familymealmate.testdata.shoppingList.TestShoppingListBuilder;
import com.farkas.familymealmate.testdata.user.TestUserFactory;
import com.farkas.familymealmate.testdata.user.TestUsers;
import com.farkas.familymealmate.testutil.ShoppingListTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class ShoppingListServiceIngtegrationTest {

    private final TestMealPlanBuilder mealPlanBuilder = new TestMealPlanBuilder();
    private final TestShoppingListBuilder shoppingListBuilder = new TestShoppingListBuilder();

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestRecipeFactory recipeFactory;

    @Autowired
    private MealPlanService mealPlanService;

    @Autowired
    private ShoppingListService shoppingListService;

    @Test
    void shouldAggregateShoppingList() {
        setupWithUserAndMealPlans();

        shoppingListService.addMealPlan(MealPlanWeek.CURRENT);
        ShoppingListDto shoppingList = shoppingListService.addMealPlan(MealPlanWeek.NEXT);
        List<ShoppingItemDto> shoppingItems = shoppingList.shoppingItems();

        assertEquals(BigDecimal.valueOf(1000), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.CHICKEN_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(800), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.GROUND_BEEF_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(5), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.TOMATO_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(1000), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.PASTA_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(2), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.CANNED_CORN_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(20), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.BUTTER_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(5), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.EGGS_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(800), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.MILK_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(200), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.SUGAR_ID, Measurement.GRAM).getQuantity());
        assertEquals(BigDecimal.valueOf(4), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.GARLIC_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(2), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.BELL_PEPPER_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(1), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.BROCCOLI_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(160), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.OATS_ID).getQuantity());

        assertNull(ShoppingListTestUtil.getDtoWithNullUnit(shoppingItems, TestRecipeIngredients.CUMIN_ID).getQuantity());
        assertNull(ShoppingListTestUtil.getDtoWithNullUnit(shoppingItems, TestRecipeIngredients.SPINACH_ID).getQuantity());

        assertEquals(16, shoppingItems.size());
    }

    @Test
    void shouldUpdateShoppingList() {
        setupUser();
        ShoppingListUpdateRequest updateRequest = getUpdateRequest();
        ShoppingListDto shoppingList = shoppingListService.update(updateRequest);
        List<ShoppingItemDto> shoppingItems = shoppingList.shoppingItems();

        assertEquals(BigDecimal.valueOf(500), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.CHICKEN_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(2), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.TOMATO_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(2), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.GARLIC_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(1), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.BELL_PEPPER_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(400), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.GROUND_BEEF_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(500), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.PASTA_ID).getQuantity());
        assertEquals(BigDecimal.ONE, ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.CANNED_CORN_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(80), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.OATS_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(250), ShoppingListTestUtil.getDto(shoppingItems, TestRecipeIngredients.MILK_ID).getQuantity());


        assertNull(ShoppingListTestUtil.getDtoWithNullUnit(shoppingItems, TestRecipeIngredients.SUGAR_ID).getQuantity());
        assertNull(ShoppingListTestUtil.getDtoWithNullUnit(shoppingItems, TestRecipeIngredients.CUMIN_ID).getQuantity());

        assertNotNull(ShoppingListTestUtil.getItemByName(shoppingItems, TestFreeTextItems.TOILET_PAPER.name()));
        assertNotNull(ShoppingListTestUtil.getItemByName(shoppingItems, TestFreeTextItems.MILK_AND_EGGS.name()));

        assertEquals(13, shoppingItems.size());
    }

    private void setupWithUserAndMealPlans() {
        setupUser();
        createMealPlans();
    }

    private void setupUser() {
        UserEntity user = userFactory.registerWithNewHousehold(TestUsers.BERTHA);
        userFactory.authenticate(user);
    }

    private void createMealPlans() {

        RecipeEntity oatsEntity = recipeFactory.createRecipe(TestRecipes.OVERNIGHT_OATS);
        RecipeEntity omeletteEntity = recipeFactory.createRecipe(TestRecipes.VEGETABLE_OMELETTE);
        RecipeEntity pancakeEntity = recipeFactory.createRecipe(TestRecipes.PANCAKES);
        RecipeEntity bologneseEntity = recipeFactory.createRecipe(TestRecipes.SPAGHETTI_BOLOGNESE);
        RecipeEntity stirFryEntity = recipeFactory.createRecipe(TestRecipes.CHICKEN_STIR_FRY);

        MealPlanUpdateRequest currentWeekMealPlan = mealPlanBuilder
                .forWeek(MealPlanWeek.CURRENT)
                .slot(TestMealNotes.OATMEAL_BREAKFAST, DayOfWeek.MONDAY, MealType.BREAKFAST, oatsEntity)
                .slot(TestMealNotes.BOLOGNESE_LUNCH, DayOfWeek.MONDAY, MealType.LUNCH, bologneseEntity)
                .slot(TestMealNotes.STIR_FRY_LUNCH, DayOfWeek.WEDNESDAY, MealType.LUNCH, stirFryEntity)
                .buildRequest();

        MealPlanUpdateRequest nextWeekMealPlan = mealPlanBuilder
                .forWeek(MealPlanWeek.NEXT)
                .slot(TestMealNotes.OATMEAL_BREAKFAST, DayOfWeek.MONDAY, MealType.BREAKFAST, oatsEntity)
                .slot(TestMealNotes.OMELETTE_BREAKFAST, DayOfWeek.TUESDAY, MealType.BREAKFAST, omeletteEntity)
                .slot(TestMealNotes.PANCAKE_BREAKFAST, DayOfWeek.THURSDAY, MealType.BREAKFAST, pancakeEntity)
                .slot(TestMealNotes.BOLOGNESE_LUNCH, DayOfWeek.MONDAY, MealType.LUNCH, bologneseEntity)
                .slot(TestMealNotes.STIR_FRY_LUNCH, DayOfWeek.WEDNESDAY, MealType.LUNCH, stirFryEntity)
                .buildRequest();

        mealPlanService.update(currentWeekMealPlan);
        mealPlanService.update(nextWeekMealPlan);
    }

    private ShoppingListUpdateRequest getUpdateRequest() {
        return shoppingListBuilder
                .addRecipeIngredients(TestRecipes.CHICKEN_STIR_FRY)
                .addRecipeIngredients(TestRecipes.SPAGHETTI_BOLOGNESE)
                .addRecipeIngredients(TestRecipes.OVERNIGHT_OATS)
                .addFreeTextItem(TestFreeTextItems.MILK_AND_EGGS)
                .addFreeTextItem(TestFreeTextItems.TOILET_PAPER)
                .buildUpdateRequest();
    }

}
