package com.farkas.familymealmate.testdata.recipe;

import com.farkas.familymealmate.service.RecipeService;
import org.springframework.stereotype.Component;

@Component
public class TestRecipeFactory {

    private final RecipeService recipeService;

    public TestRecipeFactory(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public Long createRecipe(TestRecipe recipe) {
        return recipeService.create(recipe.createRequest()).getId();
    }
}
