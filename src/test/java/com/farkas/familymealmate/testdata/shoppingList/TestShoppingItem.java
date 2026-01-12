package com.farkas.familymealmate.testdata.shoppingList;

import com.farkas.familymealmate.model.enums.Measurement;
import com.farkas.familymealmate.testdata.recipe.TestRecipeIngredient;
import com.farkas.familymealmate.util.UnitConversionUtil;

import java.math.BigDecimal;

public record TestShoppingItem(
        String name,
        String note,
        boolean checked,
        Long ingredientId,
        BigDecimal quantity,
        Measurement measurement
) {

    public TestShoppingItem(TestRecipeIngredient recipeIngredient) {
        this(
                null,
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
        if (recipeIngredient.measurement() != null) {
            return UnitConversionUtil.shouldKeepQuantity(recipeIngredient.measurement()) ? recipeIngredient.quantity() : null;
        }
        return null;
    }

    private static Measurement getMeasurement(TestRecipeIngredient recipeIngredient) {
        if (recipeIngredient.measurement() != null) {
            return UnitConversionUtil.shouldKeepQuantity(recipeIngredient.measurement()) ? recipeIngredient.measurement() : null;
        }
        return null;
    }

}
