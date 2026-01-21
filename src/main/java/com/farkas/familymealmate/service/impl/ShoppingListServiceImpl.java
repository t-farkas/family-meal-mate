package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.mapper.shoppinglist.ShoppingListMapper;
import com.farkas.familymealmate.model.dto.VersionDto;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingItemUpdateRequest;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListDto;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListUpdateRequest;
import com.farkas.familymealmate.model.entity.household.HouseholdEntity;
import com.farkas.familymealmate.model.entity.masterdata.IngredientEntity;
import com.farkas.familymealmate.model.entity.mealplan.MealPlanEntity;
import com.farkas.familymealmate.model.entity.recipe.RecipeIngredientEntity;
import com.farkas.familymealmate.model.entity.shoppinglist.ShoppingItemEntity;
import com.farkas.familymealmate.model.entity.shoppinglist.ShoppingListEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.repository.IngredientRepository;
import com.farkas.familymealmate.repository.RecipeRepository;
import com.farkas.familymealmate.repository.ShoppingListRepository;
import com.farkas.familymealmate.security.CurrentUserHelper;
import com.farkas.familymealmate.service.MealPlanService;
import com.farkas.familymealmate.service.ShoppingListService;
import com.farkas.familymealmate.service.aggregation.ShoppingItemAggregator;
import com.farkas.familymealmate.util.AggregationUtil;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ShoppingListServiceImpl implements ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final MealPlanService mealPlanService;
    private final ShoppingListMapper mapper;

    @Value("${shoppinglist.max-aggregation-retries:3}")
    private int maxRetries;

    @Override
    public void create(HouseholdEntity household) {
        Optional<ShoppingListEntity> shoppingList = shoppingListRepository.findByHouseholdId(household.getId());

        if (shoppingList.isEmpty()) {
            ShoppingListEntity entity = new ShoppingListEntity();
            entity.setHousehold(household);
            shoppingListRepository.save(entity);
        }
    }

    @Override
    public ShoppingListDto get() {
        Long householdId = CurrentUserHelper.getCurrentHousehold().getId();
        ShoppingListEntity shoppingList = getShoppingListWithItemsAndIngredients(householdId);

        return mapper.toDto(shoppingList);
    }

    @Override
    public ShoppingListDto update(ShoppingListUpdateRequest updateRequest) {
        Long householdId = CurrentUserHelper.getCurrentHousehold().getId();
        ShoppingListEntity shoppingList = getShoppingListWithItems(householdId);
        versionCheck(shoppingList, updateRequest);

        updateShoppingList(updateRequest, shoppingList);

        try {
            shoppingList.markDirty();
            shoppingListRepository.save(shoppingList);
            return mapper.toDto(getShoppingListWithItemsAndIngredients(householdId));
        } catch (ObjectOptimisticLockingFailureException exception) {
            throw new ServiceException(ErrorCode.SHOPPING_LIST_VERSION_MISMATCH);
        }
    }

    @Override
    public ShoppingListDto addMealPlan(MealPlanWeek week) {

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            Long householdId = CurrentUserHelper.getCurrentHousehold().getId();

            ShoppingListEntity shoppingList = getShoppingListWithItemsAndIngredients(householdId);
            MealPlanEntity mealPlan = mealPlanService.getFullEntity(week);

            List<ShoppingItemEntity> allItems = mergeShoppingListWithMealPlan(mealPlan, shoppingList);
            List<ShoppingItemEntity> aggregated = ShoppingItemAggregator.aggregate(allItems);

            aggregated.forEach(item -> item.setShoppingList(shoppingList));

            shoppingList.getShoppingItems().clear();
            shoppingList.getShoppingItems().addAll(aggregated);

            try {
                shoppingList.markDirty();
                shoppingListRepository.save(shoppingList);
                return mapper.toDto(getShoppingListWithItemsAndIngredients(householdId));
            } catch (OptimisticLockException exception) {
                if (attempt == maxRetries) {
                    throw new ServiceException(ErrorCode.SHOPPING_LIST_VERSION_MISMATCH);
                }
                log.info("Optimistic lock conflict, retrying attempt {}/{}", attempt, maxRetries);
            }
        }
        throw new ServiceException(ErrorCode.SHOPPING_LIST_VERSION_MISMATCH);
    }

    @Override
    public VersionDto getVersion() {
        Long householdId = CurrentUserHelper.getCurrentHousehold().getId();
        ShoppingListEntity shoppingList = getShoppingListReference(householdId);
        return new VersionDto(shoppingList.getVersion());
    }

    private void versionCheck(ShoppingListEntity shoppingList, ShoppingListUpdateRequest updateRequest) {
        if (!shoppingList.getVersion().equals(updateRequest.getVersion())) {
            throw new ServiceException(ErrorCode.SHOPPING_LIST_VERSION_MISMATCH);
        }
    }

    private void updateShoppingList(ShoppingListUpdateRequest updateRequest, ShoppingListEntity shoppingList) {

        shoppingList.setNote(updateRequest.getNote());
        shoppingList.getShoppingItems().clear();
        shoppingList.getShoppingItems().addAll(mapShoppingItems(shoppingList, updateRequest));
    }

    private List<ShoppingItemEntity> mapShoppingItems(ShoppingListEntity shoppingList, ShoppingListUpdateRequest updateRequest) {
        Map<Long, IngredientEntity> ingredientById = getIngredientMap(updateRequest);


        List<ShoppingItemEntity> itemsToSave = updateRequest.getShoppingItems().stream()
                .map(item -> createShoppingItem(ingredientById, shoppingList, item))
                .collect(Collectors.toList());

        return ShoppingItemAggregator.aggregate(itemsToSave);
    }

    private Map<Long, IngredientEntity> getIngredientMap(ShoppingListUpdateRequest updateRequest) {
        Set<Long> ingredientIds = updateRequest.getShoppingItems().stream()
                .filter(this::isIngredientBased)
                .map(ShoppingItemUpdateRequest::getIngredientId)
                .collect(Collectors.toSet());

        return ingredientRepository.findAllById(ingredientIds).stream()
                .collect(Collectors.toMap(IngredientEntity::getId, Function.identity()));
    }

    private ShoppingItemEntity createShoppingItem(Map<Long, IngredientEntity> ingredientById, ShoppingListEntity shoppingListEntity, ShoppingItemUpdateRequest item) {
        ShoppingItemEntity entity = new ShoppingItemEntity();
        entity.setNote(item.getNote());
        entity.setShoppingList(shoppingListEntity);
        entity.setChecked(item.isChecked());

        if (isIngredientBased(item)) {
            entity.setIngredient(getIngredient(ingredientById, item.getIngredientId()));
            entity.setQuantity(item.getQuantity());
            entity.setMeasurement(item.getMeasurement());
        } else if (isFreeTextItem(item)) {
            entity.setName(item.getName());
        } else {
            throw new ServiceException(ErrorCode.SHOPPING_ITEM_INCOMPLETE_DETAILS);
        }
        return entity;
    }

    private boolean isIngredientBased(ShoppingItemUpdateRequest item) {
        return item.getIngredientId() != null && item.getName() == null;
    }

    private boolean isFreeTextItem(ShoppingItemUpdateRequest item) {
        return item.getIngredientId() == null && item.getName() != null && !item.getName().isBlank();
    }

    private IngredientEntity getIngredient(Map<Long, IngredientEntity> ingredientById, Long ingredientId) {
        IngredientEntity ingredientEntity = ingredientById.get(ingredientId);
        if (ingredientEntity == null) {
            throw new ServiceException(ErrorCode.INGREDIENT_NOT_FOUND.format(ingredientId), ErrorCode.INGREDIENT_NOT_FOUND);
        }
        return ingredientEntity;
    }

    private List<ShoppingItemEntity> mergeShoppingListWithMealPlan(MealPlanEntity mealPlan, ShoppingListEntity shoppingList) {
        List<RecipeIngredientEntity> ingredientsByRecipeIdIn = getMealPlanIngredients(mealPlan);

        List<ShoppingItemEntity> itemsToAdd = ingredientsByRecipeIdIn.stream()
                .map(this::mapRecipeIngredient)
                .toList();

        List<ShoppingItemEntity> allItems = new ArrayList<>(shoppingList.getShoppingItems());
        allItems.addAll(itemsToAdd);
        return allItems;
    }

    private List<RecipeIngredientEntity> getMealPlanIngredients(MealPlanEntity mealPlan) {
        Set<Long> recipeIds = mealPlan.getMealSlots().stream()
                .map(slot -> slot.getRecipe().getId())
                .collect(Collectors.toSet());

        return recipeRepository.findAllRecipeIngredientByRecipeId(recipeIds);
    }

    private ShoppingListEntity getShoppingListReference(Long householdId) {
        return shoppingListRepository.findByHouseholdId(householdId)
                .orElseThrow(() -> new ServiceException(ErrorCode.SHOPPING_LIST_NOT_FOUND));
    }

    private ShoppingListEntity getShoppingListWithItemsAndIngredients(Long householdId) {
        return shoppingListRepository.findWithShoppingItemsAndIngredientsByHouseholdId(householdId)
                .orElseThrow(() -> new ServiceException(ErrorCode.SHOPPING_LIST_NOT_FOUND));
    }

    private ShoppingListEntity getShoppingListWithItems(Long householdId) {
        return shoppingListRepository.findWithShoppingItemsByHouseholdId(householdId)
                .orElseThrow(() -> new ServiceException(ErrorCode.SHOPPING_LIST_NOT_FOUND));
    }

    private ShoppingItemEntity mapRecipeIngredient(RecipeIngredientEntity recipeIngredient) {
        ShoppingItemEntity item = new ShoppingItemEntity();
        item.setIngredient(recipeIngredient.getIngredient());

        if (AggregationUtil.isAggregatable(recipeIngredient.getQuantity(), recipeIngredient.getMeasurement())) {
            item.setQuantity(recipeIngredient.getQuantity());
            item.setMeasurement(recipeIngredient.getMeasurement());
        }

        return item;
    }

}
