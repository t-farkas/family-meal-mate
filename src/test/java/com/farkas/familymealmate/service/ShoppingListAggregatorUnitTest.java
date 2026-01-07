package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.entity.MealPlanEntity;
import com.farkas.familymealmate.model.entity.ShoppingListEntity;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.model.enums.MealType;
import com.farkas.familymealmate.service.impl.ShoppingListAggregator;
import com.farkas.familymealmate.testdata.mealplan.TestMealPlanBuilder;
import com.farkas.familymealmate.testdata.recipe.TestRecipeIngredients;
import com.farkas.familymealmate.testdata.recipe.TestRecipes;
import com.farkas.familymealmate.testdata.shoppingList.TestFreeTextItems;
import com.farkas.familymealmate.testdata.shoppingList.TestShoppingListBuilder;
import com.farkas.familymealmate.testutil.ShoppingListTestUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingListAggregatorUnitTest {

    private final TestMealPlanBuilder mealPlanBuilder = new TestMealPlanBuilder();
    private final TestShoppingListBuilder shoppingListBuilder = new TestShoppingListBuilder();

    private final ShoppingListAggregator aggregator = new ShoppingListAggregator();

    @Test
    void aggregatesMealPlanIntoShoppingList() {

        MealPlanEntity mealPlan = getMealPlanEntity();
        ShoppingListEntity shoppingList = getShoppingListEntity();

        aggregator.aggregate(shoppingList, mealPlan);

        assertEquals(BigDecimal.valueOf(550), ShoppingListTestUtil.getItemByIngredientId(shoppingList, TestRecipeIngredients.MILK_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(5), ShoppingListTestUtil.getItemByIngredientId(shoppingList, TestRecipeIngredients.EGGS_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(2), ShoppingListTestUtil.getItemByIngredientId(shoppingList, TestRecipeIngredients.TOMATO_ID).getQuantity());

        assertNotNull(ShoppingListTestUtil.getItemByIngredientId(shoppingList, TestRecipeIngredients.OATS_ID));
        assertEquals(BigDecimal.valueOf(80), ShoppingListTestUtil.getItemByIngredientId(shoppingList, TestRecipeIngredients.OATS_ID).getQuantity());
        assertNotNull(ShoppingListTestUtil.getItemByIngredientId(shoppingList, TestRecipeIngredients.BROCCOLI_ID));
        assertEquals(BigDecimal.valueOf(1), ShoppingListTestUtil.getItemByIngredientId(shoppingList, TestRecipeIngredients.BROCCOLI_ID).getQuantity());

        assertEquals(TestFreeTextItems.MILK_AND_EGGS.note(),
                ShoppingListTestUtil.getItemByIngredientName(shoppingList, TestFreeTextItems.MILK_AND_EGGS.name()).getNote());

        assertEquals(TestFreeTextItems.TOILET_PAPER.note(),
                ShoppingListTestUtil.getItemByIngredientName(shoppingList, TestFreeTextItems.TOILET_PAPER.name()).getNote());

        assertNull(ShoppingListTestUtil.getItemByIngredientId(shoppingList, TestRecipeIngredients.CUMIN_ID).getQuantity());
        assertNull(ShoppingListTestUtil.getItemByIngredientId(shoppingList, TestRecipeIngredients.SPINACH_ID).getQuantity());

        assertEquals(14, shoppingList.getShoppingItems().size());

    }

    private ShoppingListEntity getShoppingListEntity() {
        return shoppingListBuilder
                .addRecipeIngredients(TestRecipes.SPAGHETTI_BOLOGNESE)
                .addRecipeIngredients(TestRecipes.PANCAKES)
                .addFreeTextItem(TestFreeTextItems.MILK_AND_EGGS)
                .addFreeTextItem(TestFreeTextItems.TOILET_PAPER)
                .buildEntity();
    }

    private MealPlanEntity getMealPlanEntity() {
        return mealPlanBuilder.
                forWeek(MealPlanWeek.CURRENT)
                .slot(1L, "First slot", DayOfWeek.MONDAY, MealType.BREAKFAST, TestRecipes.OVERNIGHT_OATS)
                .slot(2L, "Second slot", DayOfWeek.TUESDAY, MealType.LUNCH, TestRecipes.VEGETABLE_OMELETTE)
                .buildEntity();
    }

}
