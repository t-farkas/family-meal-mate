package com.farkas.familymealmate.testdata.recipe;

import com.farkas.familymealmate.model.dto.recipe.RecipeCreateRequest;
import com.farkas.familymealmate.model.dto.recipe.ingredient.RecipeIngredientCreateRequestDto;
import com.farkas.familymealmate.model.entity.RecipeEntity;

import java.util.List;

public record TestRecipe(
        Long id,
        String title,
        List<String> notes,
        List<String> instructions,
        List<TestRecipeIngredient> ingredients
) {

    public TestRecipe(RecipeEntity entity) {
        this(
                entity.getId(),
                entity.getTitle(),
                entity.getNotes(),
                entity.getInstructions(),
                mapIngredients(entity));
    }

    private static List<TestRecipeIngredient> mapIngredients(RecipeEntity entity) {
        return entity.getIngredients().stream()
                .map(ingredient -> new TestRecipeIngredient(
                        ingredient.getId(),
                        ingredient.getIngredient().getName(),
                        ingredient.getQuantity(),
                        ingredient.getQuantitativeMeasurement(),
                        ingredient.getIngredient().getCategory()
                )).toList();
    }

    public RecipeCreateRequest createRequest() {

        List<RecipeIngredientCreateRequestDto> ingredientList = ingredients.stream()
                .map(ingredient -> {
                    RecipeIngredientCreateRequestDto requestDto = new RecipeIngredientCreateRequestDto();
                    requestDto.setIngredientId(ingredient.ingredientId());
                    requestDto.setQuantity(ingredient.quantity());
                    requestDto.setQualitativeMeasurement(ingredient.qualitativeMeasurement());
                    requestDto.setQuantitativeMeasurement(ingredient.quantitativeMeasurement());

                    return requestDto;
                }).toList();


        return RecipeCreateRequest.builder()
                .title(title)
                .notes(notes)
                .instructions(instructions)
                .ingredients(ingredientList)
                .build();
    }
}
