package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.mapper.RecipeMapper;
import com.farkas.familymealmate.model.dto.recipe.RecipeCreateRequest;
import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.dto.recipe.RecipeListDto;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.model.entity.TagEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.repository.RecipeRepository;
import com.farkas.familymealmate.repository.TagRepository;
import com.farkas.familymealmate.security.CurrentUserService;
import com.farkas.familymealmate.security.annotation.CheckHouseholdAccess;
import com.farkas.familymealmate.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final CurrentUserService currentUserService;
    private final RecipeMapper recipeMapper;
    private final TagRepository tagRepository;

    @Override
    public void create(RecipeCreateRequest recipeCreateRequest) {

        RecipeEntity recipe = getRecipeEntity(recipeCreateRequest);
        recipeRepository.save(recipe);
    }

    @Override
    public List<RecipeListDto> list() {
        Long householdId = currentUserService.getCurrentHousehold().getId();
        List<RecipeEntity> recipes = recipeRepository.findAllByHouseholdId(householdId);

        return recipeMapper.toDtoList(recipes);
    }

    @Override
    public List<RecipeListDto> list(Set<Long> tagIds) {
        Long householdId = currentUserService.getCurrentHousehold().getId();
        List<RecipeEntity> recipes = recipeRepository.findAllByHouseholdIdAndTagIds(householdId, tagIds);

        return recipeMapper.toDtoList(recipes);
    }

    @Override
    @CheckHouseholdAccess
    public RecipeDetailsDto get(Long id) {
        RecipeEntity recipe = getRecipe(id);
        return recipeMapper.toRecipeDetails(recipe);
    }

    @Override
    @CheckHouseholdAccess
    public void delete(Long id) {
        RecipeEntity recipe = getRecipe(id);
        recipeRepository.delete(recipe);
    }

    private RecipeEntity getRecipe(Long id) {
        return recipeRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorCode.RECIPE_NOT_FOUND.format(id), ErrorCode.RECIPE_NOT_FOUND));
    }

    private RecipeEntity getRecipeEntity(RecipeCreateRequest recipeCreateRequest) {
        RecipeEntity recipe = recipeMapper.toEntity(recipeCreateRequest);
        recipe.setCreatedBy(currentUserService.getCurrentFamilyMember());
        recipe.setHousehold(currentUserService.getCurrentHousehold());

        Set<TagEntity> tags = recipeCreateRequest.getTagIds().stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new ServiceException(
                                ErrorCode.TAG_NOT_FOUND.format(tagId),
                                ErrorCode.TAG_NOT_FOUND)))
                .collect(Collectors.toSet());
        recipe.setTags(tags);
        return recipe;
    }
}
