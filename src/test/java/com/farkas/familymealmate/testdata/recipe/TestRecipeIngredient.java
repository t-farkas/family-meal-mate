package com.farkas.familymealmate.testdata.recipe;

import com.farkas.familymealmate.model.enums.IngredientCategory;
import com.farkas.familymealmate.model.enums.QualitativeMeasurement;
import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;

import java.math.BigDecimal;

public record TestRecipeIngredient(
        Long ingredientId,
        String ingredientName,
        BigDecimal quantity,
        QuantitativeMeasurement quantitativeMeasurement,
        QualitativeMeasurement qualitativeMeasurement,
        IngredientCategory category
) {

    public TestRecipeIngredient(Long ingredientId, String ingredientName, BigDecimal quantity, QuantitativeMeasurement quantitativeMeasurement, IngredientCategory category) {
        this(
                ingredientId,
                ingredientName,
                quantity,
                quantitativeMeasurement,
                null,
                category);
    }

    public TestRecipeIngredient(Long ingredientId, String ingredientName, QualitativeMeasurement qualitativeMeasurement, IngredientCategory category) {
        this(
                ingredientId,
                ingredientName,
                null,
                null,
                qualitativeMeasurement,
                category);
    }
}
