package com.farkas.familymealmate.testdata.recipe;

import com.farkas.familymealmate.model.dto.recipe.RecipeCreateRequest;
import com.farkas.familymealmate.model.dto.recipe.ingredient.RecipeIngredientCreateRequestDto;

import java.util.List;

public record TestRecipe(
        String title,
        List<String> instructions,
        List<RecipeIngredientCreateRequestDto> ingredients
) {

    public RecipeCreateRequest createRequest() {
        return RecipeCreateRequest.builder()
                .title(title)
                .instructions(instructions)
                .ingredients(ingredients)
                .build();
    }
}
