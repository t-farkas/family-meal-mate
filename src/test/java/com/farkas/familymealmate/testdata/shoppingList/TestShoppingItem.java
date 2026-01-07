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
                UnitConversionUtil.shouldKeepQuantity(recipeIngredient.quantitativeMeasurement()) ? recipeIngredient.quantity() : null,
                UnitConversionUtil.shouldKeepQuantity(recipeIngredient.quantitativeMeasurement()) ? recipeIngredient.quantitativeMeasurement() : null);
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

}
