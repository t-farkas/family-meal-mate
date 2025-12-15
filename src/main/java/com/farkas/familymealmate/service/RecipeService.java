package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.recipe.RecipeCreateRequest;
import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.dto.recipe.RecipeListDto;

import java.util.List;
import java.util.Set;

public interface RecipeService {

    void create(RecipeCreateRequest recipeCreateRequest);
    List<RecipeListDto> list();
    List<RecipeListDto> list(Set<Long> tagIds);
    RecipeDetailsDto get(Long id);
    void delete(Long id);
}
