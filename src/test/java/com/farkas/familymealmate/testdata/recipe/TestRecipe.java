package com.farkas.familymealmate.testdata.recipe;

import com.farkas.familymealmate.model.dto.recipe.RecipeCreateRequest;

import java.util.List;

public record TestRecipe(
        String title,
        List<String> instructions
) {

    public RecipeCreateRequest createRequest() {
        return RecipeCreateRequest.builder()
                .title(title)
                .instructions(instructions)
                .build();
    }
}
