package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.PagingResponse;
import com.farkas.familymealmate.model.dto.recipe.RecipeCreateRequest;
import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.dto.recipe.RecipeFilterRequest;
import com.farkas.familymealmate.model.dto.recipe.RecipeListDto;
import com.farkas.familymealmate.model.entity.RecipeEntity;

public interface RecipeService {

    RecipeDetailsDto create(RecipeCreateRequest recipeCreateRequest);

    PagingResponse<RecipeListDto> list(RecipeFilterRequest request);

    RecipeDetailsDto get(Long id);

    RecipeEntity getEntity(Long id);

    void delete(Long id);
}
