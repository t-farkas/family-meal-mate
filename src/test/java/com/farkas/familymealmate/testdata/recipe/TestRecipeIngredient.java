package com.farkas.familymealmate.testdata.recipe;

import com.farkas.familymealmate.model.enums.IngredientCategory;
import com.farkas.familymealmate.model.enums.Measurement;

import java.math.BigDecimal;

public record TestRecipeIngredient(
        Long ingredientId,
        String ingredientName,
        BigDecimal quantity,
        Measurement measurement,
        IngredientCategory category
) {
}
