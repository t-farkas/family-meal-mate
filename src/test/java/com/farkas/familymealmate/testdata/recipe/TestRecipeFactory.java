package com.farkas.familymealmate.testdata.recipe;

import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.service.RecipeService;
import org.springframework.stereotype.Component;

@Component
public class TestRecipeFactory {

    private final RecipeService recipeService;

    public TestRecipeFactory(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public RecipeEntity createRecipe(TestRecipe recipe) {
        RecipeDetailsDto recipeDto = recipeService.create(recipe.createRequest());
        return getEntity(recipeDto.getId());
    }

    public RecipeEntity getEntity(Long id){
        return recipeService.getEntity(id);
    }
}
