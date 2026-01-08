package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.entity.MealPlanEntity;
import com.farkas.familymealmate.model.entity.ShoppingItemEntity;
import com.farkas.familymealmate.model.entity.ShoppingListEntity;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.model.enums.MealType;
import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;
import com.farkas.familymealmate.service.impl.ShoppingListAggregator;
import com.farkas.familymealmate.testdata.mealplan.TestMealPlanBuilder;
import com.farkas.familymealmate.testdata.recipe.TestRecipe;
import com.farkas.familymealmate.testdata.recipe.TestRecipeIngredients;
import com.farkas.familymealmate.testdata.recipe.TestRecipes;
import com.farkas.familymealmate.testdata.shoppingList.TestShoppingListBuilder;
import com.farkas.familymealmate.testutil.ShoppingListTestUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingListAggregatorUnitTest {

    private final TestMealPlanBuilder mealPlanBuilder = new TestMealPlanBuilder();
    private final TestShoppingListBuilder shoppingListBuilder = new TestShoppingListBuilder();

    private final ShoppingListAggregator aggregator = new ShoppingListAggregator();

    @Test
    void aggregatesMealPlanIntoShoppingList() {

        MealPlanEntity mealPlan = getCurrentMealPlanEntity();
        ShoppingListEntity shoppingList = getShoppingListEntity(TestRecipes.SPAGHETTI_BOLOGNESE, TestRecipes.PANCAKES);

        aggregator.aggregate(shoppingList, mealPlan);

        assertEquals(BigDecimal.valueOf(550), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.MILK_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(5), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.EGGS_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(2), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.TOMATO_ID).getQuantity());

        assertNotNull(ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.OATS_ID));
        assertEquals(BigDecimal.valueOf(80), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.OATS_ID).getQuantity());
        assertNotNull(ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.BROCCOLI_ID));
        assertEquals(BigDecimal.valueOf(1), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.BROCCOLI_ID).getQuantity());

        assertNull(ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.CUMIN_ID).getQuantity());
        assertNull(ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.SPINACH_ID).getQuantity());

        assertEquals(12, shoppingList.getShoppingItems().size());

    }

    @Test
    void aggregatesPiecesWithGrams() {
        MealPlanEntity mealPlan = getNextMealPlanEntity();
        ShoppingListEntity shoppingList = getShoppingListEntity(TestRecipes.VEGETABLE_OMELETTE);

        aggregator.aggregate(shoppingList, mealPlan);

        assertEquals(BigDecimal.valueOf(3), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.EGGS_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(200), ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.RICE_ID).getQuantity());

        List<ShoppingItemEntity> tomatoList = ShoppingListTestUtil.getItemListByIngredientId(shoppingList, TestRecipeIngredients.TOMATO_ID);
        assertEquals(2, tomatoList.size());
        assertEquals(BigDecimal.valueOf(300), ShoppingListTestUtil.getByMeasurement(tomatoList, QuantitativeMeasurement.GRAM).getQuantity());
        assertEquals(BigDecimal.valueOf(1), ShoppingListTestUtil.getByMeasurement(tomatoList, QuantitativeMeasurement.PIECE).getQuantity());

        List<ShoppingItemEntity> broccoliList = ShoppingListTestUtil.getItemListByIngredientId(shoppingList, TestRecipeIngredients.BROCCOLI_ID);
        assertEquals(2, broccoliList.size());
        assertEquals(BigDecimal.valueOf(150), ShoppingListTestUtil.getByMeasurement(broccoliList, QuantitativeMeasurement.GRAM).getQuantity());
        assertEquals(BigDecimal.valueOf(1), ShoppingListTestUtil.getByMeasurement(broccoliList, QuantitativeMeasurement.PIECE).getQuantity());

        assertNull(ShoppingListTestUtil.getFirstItemByIngredientId(shoppingList, TestRecipeIngredients.SPINACH_ID).getQuantity());

        assertEquals(7, shoppingList.getShoppingItems().size());

    }

    private ShoppingListEntity getShoppingListEntity(TestRecipe... recipes) {
        return shoppingListBuilder
                .addRecipes(recipes)
                .buildEntity();
    }

    private MealPlanEntity getCurrentMealPlanEntity() {
        return mealPlanBuilder.
                forWeek(MealPlanWeek.CURRENT)
                .slot(1L, "First slot", DayOfWeek.MONDAY, MealType.BREAKFAST, TestRecipes.OVERNIGHT_OATS)
                .slot(2L, "Second slot", DayOfWeek.TUESDAY, MealType.LUNCH, TestRecipes.VEGETABLE_OMELETTE)
                .buildEntity();
    }

    private MealPlanEntity getNextMealPlanEntity() {
        return mealPlanBuilder.
                forWeek(MealPlanWeek.NEXT)
                .slot(1L, "First slot", DayOfWeek.MONDAY, MealType.BREAKFAST, TestRecipes.VEGGIE_BOWL)
                .buildEntity();
    }


}
