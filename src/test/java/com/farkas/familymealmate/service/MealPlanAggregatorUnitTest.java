package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.entity.ShoppingItemEntity;
import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;
import com.farkas.familymealmate.service.aggregation.ShoppingItemAggregator;
import com.farkas.familymealmate.testdata.recipe.TestRecipe;
import com.farkas.familymealmate.testdata.recipe.TestRecipeIngredients;
import com.farkas.familymealmate.testdata.recipe.TestRecipes;
import com.farkas.familymealmate.testdata.shoppingList.TestShoppingListBuilder;
import com.farkas.familymealmate.testutil.ShoppingListTestUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MealPlanAggregatorUnitTest {

    private final TestShoppingListBuilder shoppingListBuilder = new TestShoppingListBuilder();

    @Test
    void aggregatesConvertableUnits() {

        List<ShoppingItemEntity> shoppingListItems = getShoppingListItems(
                TestRecipes.SPAGHETTI_BOLOGNESE,
                TestRecipes.PANCAKES,
                TestRecipes.OVERNIGHT_OATS,
                TestRecipes.VEGETABLE_OMELETTE);


        List<ShoppingItemEntity> aggregated = ShoppingItemAggregator.aggregate(shoppingListItems);

        assertEquals(BigDecimal.valueOf(550), ShoppingListTestUtil.getFirstItemByIngredientId(aggregated, TestRecipeIngredients.MILK_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(5), ShoppingListTestUtil.getFirstItemByIngredientId(aggregated, TestRecipeIngredients.EGGS_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(2), ShoppingListTestUtil.getFirstItemByIngredientId(aggregated, TestRecipeIngredients.TOMATO_ID).getQuantity());

        assertNotNull(ShoppingListTestUtil.getFirstItemByIngredientId(aggregated, TestRecipeIngredients.OATS_ID));
        assertEquals(BigDecimal.valueOf(80), ShoppingListTestUtil.getFirstItemByIngredientId(aggregated, TestRecipeIngredients.OATS_ID).getQuantity());
        assertNotNull(ShoppingListTestUtil.getFirstItemByIngredientId(aggregated, TestRecipeIngredients.BROCCOLI_ID));
        assertEquals(BigDecimal.valueOf(1), ShoppingListTestUtil.getFirstItemByIngredientId(aggregated, TestRecipeIngredients.BROCCOLI_ID).getQuantity());

        assertNull(ShoppingListTestUtil.getFirstItemByIngredientId(aggregated, TestRecipeIngredients.CUMIN_ID).getQuantity());
        assertNull(ShoppingListTestUtil.getFirstItemByIngredientId(aggregated, TestRecipeIngredients.SPINACH_ID).getQuantity());

        assertEquals(12, aggregated.size());

    }

    @Test
    void aggregatesPiecesWithGrams() {

        List<ShoppingItemEntity> shoppingListItems = getShoppingListItems(
                TestRecipes.VEGETABLE_OMELETTE,
                TestRecipes.VEGGIE_BOWL);

        List<ShoppingItemEntity> aggregated = ShoppingItemAggregator.aggregate(shoppingListItems);

        assertEquals(BigDecimal.valueOf(3), ShoppingListTestUtil.getFirstItemByIngredientId(aggregated, TestRecipeIngredients.EGGS_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(200), ShoppingListTestUtil.getFirstItemByIngredientId(aggregated, TestRecipeIngredients.RICE_ID).getQuantity());

        List<ShoppingItemEntity> tomatoList = ShoppingListTestUtil.getItemListByIngredientId(aggregated, TestRecipeIngredients.TOMATO_ID);
        assertEquals(2, tomatoList.size());
        assertEquals(BigDecimal.valueOf(300), ShoppingListTestUtil.getByMeasurement(tomatoList, QuantitativeMeasurement.GRAM).getQuantity());
        assertEquals(BigDecimal.valueOf(1), ShoppingListTestUtil.getByMeasurement(tomatoList, QuantitativeMeasurement.PIECE).getQuantity());

        List<ShoppingItemEntity> broccoliList = ShoppingListTestUtil.getItemListByIngredientId(aggregated, TestRecipeIngredients.BROCCOLI_ID);
        assertEquals(2, broccoliList.size());
        assertEquals(BigDecimal.valueOf(150), ShoppingListTestUtil.getByMeasurement(broccoliList, QuantitativeMeasurement.GRAM).getQuantity());
        assertEquals(BigDecimal.valueOf(1), ShoppingListTestUtil.getByMeasurement(broccoliList, QuantitativeMeasurement.PIECE).getQuantity());

        assertNull(ShoppingListTestUtil.getFirstItemByIngredientId(aggregated, TestRecipeIngredients.SPINACH_ID).getQuantity());

        assertEquals(7, aggregated.size());

    }

    private List<ShoppingItemEntity> getShoppingListItems(TestRecipe... recipes) {
        return shoppingListBuilder
                .addRecipes(recipes)
                .buildAggregateRequest();
    }

}
