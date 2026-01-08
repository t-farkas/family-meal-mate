package com.farkas.familymealmate.testdata.shoppingList;

import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;
import com.farkas.familymealmate.testdata.recipe.TestRecipeIngredient;
import com.farkas.familymealmate.util.UnitConversionUtil;

import java.math.BigDecimal;

public record TestShoppingItem(
        String name,
        String note,
        boolean checked,
        Long ingredientId,
        BigDecimal quantity,
        QuantitativeMeasurement quantitativeMeasurement
) {

    public TestShoppingItem(TestRecipeIngredient recipeIngredient) {
        this(
                recipeIngredient.ingredientName(),
                null,
                false,
                recipeIngredient.ingredientId(),
                getQuantity(recipeIngredient),
                getMeasurement(recipeIngredient));
    }

    public TestShoppingItem(String name, String note) {
        this(
                name,
                note,
                false,
                null,
                null,
                null
        );
    }

    private static BigDecimal getQuantity(TestRecipeIngredient recipeIngredient) {
        if (recipeIngredient.quantitativeMeasurement() != null) {
            return UnitConversionUtil.shouldKeepQuantity(recipeIngredient.quantitativeMeasurement()) ? recipeIngredient.quantity() : null;
        }
        return null;
    }

    private static QuantitativeMeasurement getMeasurement(TestRecipeIngredient recipeIngredient) {
        if (recipeIngredient.quantitativeMeasurement() != null) {
            return UnitConversionUtil.shouldKeepQuantity(recipeIngredient.quantitativeMeasurement()) ? recipeIngredient.quantitativeMeasurement() : null;
        }
        return null;
    }

}
