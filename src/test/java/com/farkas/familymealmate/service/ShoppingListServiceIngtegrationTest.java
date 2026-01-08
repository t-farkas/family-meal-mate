package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListDto;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.model.enums.MealType;
import com.farkas.familymealmate.testdata.mealplan.TestMealNotes;
import com.farkas.familymealmate.testdata.mealplan.TestMealPlanBuilder;
import com.farkas.familymealmate.testdata.recipe.TestRecipeFactory;
import com.farkas.familymealmate.testdata.recipe.TestRecipeIngredients;
import com.farkas.familymealmate.testdata.recipe.TestRecipes;
import com.farkas.familymealmate.testdata.user.TestUserFactory;
import com.farkas.familymealmate.testdata.user.TestUsers;
import com.farkas.familymealmate.testutil.ShoppingListTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
public class ShoppingListServiceIngtegrationTest {

    private final TestMealPlanBuilder mealPlanBuilder = new TestMealPlanBuilder();

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

        assertEquals(BigDecimal.valueOf(1000), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.CHICKEN_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(800), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.GROUND_BEEF_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(5), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.TOMATO_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(1000), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.PASTA_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(2), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.CANNED_CORN_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(20), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.BUTTER_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(5), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.EGGS_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(800), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.MILK_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(200), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.SUGAR_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(4), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.GARLIC_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(2), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.BELL_PEPPER_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(1), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.BROCCOLI_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(160), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.OATS_ID).getQuantity());

        assertNull(ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.CUMIN_ID).getQuantity());
        assertNull(ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.SPINACH_ID).getQuantity());

        assertEquals(15, shoppingList.getShoppingItems().size());
    }

    private void setupWithUserAndMealPlans() {
        setupUser();
        createMealPlans();
    }

    private void setupUser() {
        UserEntity user = userFactory.registerWithNewHousehold(TestUsers.BERTHA);
        userFactory.authenticate(user);
        shoppingListService.createForHousehold(user.getFamilyMember().getHousehold());
    }

    private void createMealPlans() {
        mealPlanService.createMealPlans();

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

        mealPlanService.editMealPlan(currentWeekMealPlan);
        mealPlanService.editMealPlan(nextWeekMealPlan);
    }


}
