package com.farkas.familymealmate.testdata.recipe;

import com.farkas.familymealmate.model.dto.recipe.RecipeCreateRequest;
import com.farkas.familymealmate.model.dto.recipe.ingredient.RecipeIngredientCreateRequestDto;

import java.util.List;

public record TestRecipe(
        Long id,
        String title,
        String note,
        List<String> instructions,
        List<TestRecipeIngredient> ingredients
) {

    public RecipeCreateRequest createRequest() {

        List<RecipeIngredientCreateRequestDto> ingredientList = ingredients.stream()
                .map(ingredient -> {
                    RecipeIngredientCreateRequestDto requestDto = new RecipeIngredientCreateRequestDto();
                    requestDto.setIngredientId(ingredient.id());
                    requestDto.setQuantity(ingredient.quantity());
                    requestDto.setQualitativeMeasurement(ingredient.qualitativeMeasurement());
                    requestDto.setQuantitativeMeasurement(ingredient.quantitativeMeasurement());

                    return requestDto;
                }).toList();


        return RecipeCreateRequest.builder()
                .title(title)
                .notes(List.of(note))
                .instructions(instructions)
                .ingredients(ingredientList)
                .build();
    }
}
