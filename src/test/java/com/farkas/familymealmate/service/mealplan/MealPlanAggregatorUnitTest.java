package com.farkas.familymealmate.service.mealplan;

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

        assertEquals(BigDecimal.valueOf(400), ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.GROUND_BEEF_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(550), ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.MILK_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(500), ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.PASTA_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(200), ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.SUGAR_ID, QuantitativeMeasurement.GRAM).getQuantity());
        assertEquals(BigDecimal.valueOf(5), ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.EGGS_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(2), ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.TOMATO_ID).getQuantity());

        assertEquals(BigDecimal.valueOf(80), ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.OATS_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(20), ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.BUTTER_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(1), ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.BROCCOLI_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(1), ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.CANNED_CORN_ID).getQuantity());

        assertNull(ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.CUMIN_ID).getQuantity());
        assertNull(ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.SPINACH_ID).getQuantity());
        assertNull(ShoppingListTestUtil.getEntityWithNullUnit(aggregated, TestRecipeIngredients.SUGAR_ID).getQuantity());

        assertEquals(13, aggregated.size());

    }

    @Test
    void aggregatesPiecesWithGrams() {

        List<ShoppingItemEntity> shoppingListItems = getShoppingListItems(
                TestRecipes.VEGETABLE_OMELETTE,
                TestRecipes.VEGGIE_BOWL);

        List<ShoppingItemEntity> aggregated = ShoppingItemAggregator.aggregate(shoppingListItems);

        assertEquals(BigDecimal.valueOf(3), ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.EGGS_ID).getQuantity());
        assertEquals(BigDecimal.valueOf(200), ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.RICE_ID).getQuantity());

        ShoppingItemEntity tomatoGram = ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.TOMATO_ID, QuantitativeMeasurement.GRAM);
        ShoppingItemEntity tomatoPiece = ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.TOMATO_ID, QuantitativeMeasurement.PIECE);
        assertEquals(BigDecimal.valueOf(300), tomatoGram.getQuantity());
        assertEquals(BigDecimal.valueOf(1), tomatoPiece.getQuantity());

        ShoppingItemEntity broccoliGram = ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.BROCCOLI_ID, QuantitativeMeasurement.GRAM);
        ShoppingItemEntity broccoliPiece = ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.BROCCOLI_ID, QuantitativeMeasurement.PIECE);
        assertEquals(BigDecimal.valueOf(150), broccoliGram.getQuantity());
        assertEquals(BigDecimal.valueOf(1), broccoliPiece.getQuantity());

        assertNull(ShoppingListTestUtil.getEntity(aggregated, TestRecipeIngredients.SPINACH_ID).getQuantity());

        assertEquals(7, aggregated.size());

    }

    private List<ShoppingItemEntity> getShoppingListItems(TestRecipe... recipes) {
        return shoppingListBuilder
                .addRecipes(recipes)
                .buildAggregateRequest();
    }

}
