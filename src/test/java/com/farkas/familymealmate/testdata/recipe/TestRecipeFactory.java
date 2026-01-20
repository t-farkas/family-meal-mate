package com.farkas.familymealmate.testdata.recipe;

import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.repository.RecipeRepository;
import com.farkas.familymealmate.service.RecipeService;
import org.springframework.stereotype.Component;

@Component
public class TestRecipeFactory {

    private final RecipeService recipeService;
    private final RecipeRepository recipeRepository;

    public TestRecipeFactory(RecipeService recipeService, RecipeRepository recipeRepository) {
        this.recipeService = recipeService;
        this.recipeRepository = recipeRepository;
    }

    public RecipeEntity createRecipe(TestRecipe recipe) {
        RecipeDetailsDto recipeDto = recipeService.create(recipe.createRequest());
        return getEntity(recipeDto.getId());
    }

    public RecipeEntity getEntity(Long id){
        return recipeRepository.findById(id).get();
    }
}
