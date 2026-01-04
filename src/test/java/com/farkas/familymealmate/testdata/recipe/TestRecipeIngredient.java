package com.farkas.familymealmate.testdata.recipe;

import com.farkas.familymealmate.model.enums.IngredientCategory;
import com.farkas.familymealmate.model.enums.QualitativeMeasurement;
import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;

import java.math.BigDecimal;

public record TestRecipeIngredient(
        Long id,
        BigDecimal quantity,
        QuantitativeMeasurement quantitativeMeasurement,
        QualitativeMeasurement qualitativeMeasurement,
        IngredientCategory category
) {

    public TestRecipeIngredient(Long id, BigDecimal quantity, QuantitativeMeasurement quantitativeMeasurement, IngredientCategory category) {
        this(id,
                quantity,
                quantitativeMeasurement,
                null,
                category);
    }

    public TestRecipeIngredient(Long id, QualitativeMeasurement qualitativeMeasurement, IngredientCategory category) {
        this(id,
                null,
                null,
                qualitativeMeasurement,
                category);
    }
}
