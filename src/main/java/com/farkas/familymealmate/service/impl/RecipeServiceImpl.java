package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.mapper.RecipeMapper;
import com.farkas.familymealmate.model.dto.PagingResponse;
import com.farkas.familymealmate.model.dto.recipe.RecipeCreateRequest;
import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.dto.recipe.RecipeFilterRequest;
import com.farkas.familymealmate.model.dto.recipe.RecipeListDto;
import com.farkas.familymealmate.model.dto.recipe.ingredient.RecipeIngredientCreateRequestDto;
import com.farkas.familymealmate.model.entity.IngredientEntity;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.model.entity.RecipeIngredientEntity;
import com.farkas.familymealmate.model.entity.TagEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.model.enums.HouseholdOwnedResourceType;
import com.farkas.familymealmate.repository.IngredientRepository;
import com.farkas.familymealmate.repository.RecipeRepository;
import com.farkas.familymealmate.repository.TagRepository;
import com.farkas.familymealmate.repository.specification.RecipeSpecificationBuilder;
import com.farkas.familymealmate.security.CurrentUserService;
import com.farkas.familymealmate.security.annotation.CheckHouseholdAccess;
import com.farkas.familymealmate.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final TagRepository tagRepository;
    private final IngredientRepository ingredientRepository;
    private final CurrentUserService currentUserService;
    private final RecipeMapper recipeMapper;

    @Override
    public RecipeDetailsDto create(RecipeCreateRequest recipeCreateRequest) {

        RecipeEntity recipe = getRecipeEntity(recipeCreateRequest);
        RecipeEntity saved = recipeRepository.save(recipe);
        return recipeMapper.toRecipeDetails(saved);
    }

    @Override
    public PagingResponse<RecipeListDto> list(RecipeFilterRequest request) {

        Specification<RecipeEntity> spec = new RecipeSpecificationBuilder()
                .byHousehold(currentUserService.getCurrentHousehold().getId())
                .byIngredientIds(request.getIngredientIds())
                .byName(request.getName())
                .byTagIds(request.getTagIds())
                .build();

        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize());

        Page<RecipeEntity> recipePage = recipeRepository.findAll(spec, pageable);
        List<RecipeListDto> recipeDtoList = recipeMapper.toDtoList(recipePage.getContent());

        return new PagingResponse<>(recipeDtoList, recipePage.getTotalElements(), recipePage.getTotalPages(), recipePage.getNumber(), recipePage.getSize());

    }

    @Override
    @CheckHouseholdAccess(type = HouseholdOwnedResourceType.RECIPE)
    public RecipeDetailsDto get(Long id) {
        RecipeEntity recipe = getRecipe(id);
        return recipeMapper.toRecipeDetails(recipe);
    }

    @Override
    @CheckHouseholdAccess(type = HouseholdOwnedResourceType.RECIPE)
    public RecipeEntity getEntity(Long id) {
        return getRecipe(id);
    }

    @Override
    @CheckHouseholdAccess(type = HouseholdOwnedResourceType.RECIPE)
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

        recipe.setTags(getTags(recipeCreateRequest.getTagIds()));
        recipe.setIngredients(getIngredients(recipeCreateRequest.getIngredients(), recipe));
        return recipe;
    }

    private List<RecipeIngredientEntity> getIngredients(List<RecipeIngredientCreateRequestDto> ingredients, RecipeEntity recipe) {
        List<RecipeIngredientEntity> recipeIngredients = new ArrayList<>();

        ingredients.forEach(ingredient -> {
            RecipeIngredientEntity recipeIngredient = new RecipeIngredientEntity();
            recipeIngredient.setQuantity(ingredient.getQuantity());
            recipeIngredient.setMeasurement(ingredient.getMeasurement());
            recipeIngredient.setRecipe(recipe);
            IngredientEntity ingredientEntity = getIngredientEntity(ingredient);
            recipeIngredient.setIngredient(ingredientEntity);
            recipeIngredients.add(recipeIngredient);

        });

        return recipeIngredients;
    }

    private IngredientEntity getIngredientEntity(RecipeIngredientCreateRequestDto ingredient) {
        return ingredientRepository.findById(ingredient.getIngredientId()).orElseThrow(
                () -> new ServiceException(ErrorCode.INGREDIENT_NOT_FOUND.format(ingredient.getIngredientId()),
                        ErrorCode.INGREDIENT_NOT_FOUND));
    }

    private Set<TagEntity> getTags(Set<Long> tagIds) {

        if (tagIds == null || tagIds.isEmpty()) {
            return null;
        }

        return tagIds.stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new ServiceException(
                                ErrorCode.TAG_NOT_FOUND.format(tagId),
                                ErrorCode.TAG_NOT_FOUND)))
                .collect(Collectors.toSet());
    }
}
