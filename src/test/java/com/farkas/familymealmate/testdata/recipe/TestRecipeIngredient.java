package com.farkas.familymealmate.testdata.recipe;

import com.farkas.familymealmate.model.entity.recipe.RecipeIngredientEntity;
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

    public RecipeIngredientEntity getRecipeIngredientEntity() {
        RecipeIngredientEntity recipeIngredientEntity = new RecipeIngredientEntity();
        recipeIngredientEntity.setId(ingredientId);
        recipeIngredientEntity.setMeasurement(measurement);
        recipeIngredientEntity.setQuantity(quantity);
        return recipeIngredientEntity;
    }
}
