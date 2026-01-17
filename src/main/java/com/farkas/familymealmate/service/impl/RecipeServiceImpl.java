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
import com.farkas.familymealmate.security.CurrentUserHelper;
import com.farkas.familymealmate.security.annotation.CheckHouseholdAccess;
import com.farkas.familymealmate.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeMapper recipeMapper;
    private final TagRepository tagRepository;

    @Override
    public RecipeDetailsDto create(RecipeCreateRequest recipeCreateRequest) {

        RecipeEntity recipe = getRecipeEntity(recipeCreateRequest);
        RecipeEntity saved = recipeRepository.save(recipe);
        return recipeMapper.toRecipeDetails(getFullRecipe(saved.getId()));
    }

    @Override
    public PagingResponse<RecipeListDto> list(RecipeFilterRequest request) {

        Specification<RecipeEntity> spec = new RecipeSpecificationBuilder()
                .byHousehold(CurrentUserHelper.getCurrentHousehold().getId())
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
        RecipeEntity recipe = getFullRecipe(id);
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

    private RecipeEntity getFullRecipe(Long id) {
        return recipeRepository.getFullEntityById(id).orElseThrow(
                () -> new ServiceException(ErrorCode.RECIPE_NOT_FOUND.format(id), ErrorCode.RECIPE_NOT_FOUND));
    }

    private RecipeEntity getRecipe(Long id) {
        return recipeRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorCode.RECIPE_NOT_FOUND.format(id), ErrorCode.RECIPE_NOT_FOUND));
    }

    private RecipeEntity getRecipeEntity(RecipeCreateRequest recipeCreateRequest) {
        RecipeEntity recipe = recipeMapper.toEntity(recipeCreateRequest);
        recipe.setCreatedBy(CurrentUserHelper.getCurrentFamilyMember());
        recipe.setHousehold(CurrentUserHelper.getCurrentHousehold());

        recipe.setTags(getTags(recipeCreateRequest.getTagIds()));
        recipe.setIngredients(getIngredients(recipeCreateRequest.getIngredients(), recipe));
        return recipe;
    }

    private List<RecipeIngredientEntity> getIngredients(List<RecipeIngredientCreateRequestDto> ingredients, RecipeEntity recipe) {
        List<RecipeIngredientEntity> recipeIngredients = new ArrayList<>();

        List<Long> ingredientIds = ingredients.stream()
                .map(RecipeIngredientCreateRequestDto::getIngredientId)
                .toList();
        Map<Long, IngredientEntity> ingredientEntityMap = ingredientRepository.findAllById(ingredientIds).stream().collect(Collectors.toMap(IngredientEntity::getId, Function.identity()));

        ingredients.forEach(ingredient -> {
            RecipeIngredientEntity recipeIngredient = new RecipeIngredientEntity();
            recipeIngredient.setQuantity(ingredient.getQuantity());
            recipeIngredient.setMeasurement(ingredient.getMeasurement());
            recipeIngredient.setRecipe(recipe);
            IngredientEntity ingredientEntity = getIngredientEntity(ingredientEntityMap, ingredient.getIngredientId());
            recipeIngredient.setIngredient(ingredientEntity);
            recipeIngredients.add(recipeIngredient);
        });

        return recipeIngredients;
    }

    private IngredientEntity getIngredientEntity(Map<Long, IngredientEntity> ingredientEntities, Long ingredientId) {
        IngredientEntity ingredientEntity = ingredientEntities.get(ingredientId);
        if (ingredientEntity == null) {
            throw new ServiceException(ErrorCode.INGREDIENT_NOT_FOUND.format(ingredientId),
                    ErrorCode.INGREDIENT_NOT_FOUND);
        }

        return ingredientEntity;
    }

    private Set<TagEntity> getTags(Set<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Collections.emptySet();
        }

        List<TagEntity> tagEntities = tagRepository.findAllById(tagIds);

        if (tagEntities.size() != tagIds.size()) {
            handleMissingTag(tagIds, tagEntities);
        }

        return new HashSet<>(tagEntities);
    }

    private void handleMissingTag(Set<Long> tagIds, List<TagEntity> tagEntities) {
        Set<Long> entityIdSet = tagEntities.stream()
                .map(TagEntity::getId)
                .collect(Collectors.toSet());

        Set<Long> missingIds = new HashSet<>(tagIds);
        missingIds.removeAll(entityIdSet);
        String missingIdStr = missingIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        throw new ServiceException(ErrorCode.TAG_NOT_FOUND.format(missingIdStr), ErrorCode.TAG_NOT_FOUND);
    }
}
