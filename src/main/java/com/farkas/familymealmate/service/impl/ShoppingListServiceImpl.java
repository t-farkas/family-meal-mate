package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.mapper.ShoppingListMapper;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingItemUpdateRequest;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListDto;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListUpdateRequest;
import com.farkas.familymealmate.model.entity.*;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.repository.IngredientRepository;
import com.farkas.familymealmate.repository.ShoppingListRepository;
import com.farkas.familymealmate.security.CurrentUserService;
import com.farkas.familymealmate.service.MealPlanService;
import com.farkas.familymealmate.service.ShoppingListService;
import com.farkas.familymealmate.service.aggregation.ShoppingItemAggregator;
import com.farkas.familymealmate.util.AggregationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingListServiceImpl implements ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final IngredientRepository ingredientRepository;
    private final CurrentUserService currentUserService;
    private final MealPlanService mealPlanService;
    private final ShoppingListMapper mapper;

    @Override
    public void createForHousehold(HouseholdEntity household) {
        Optional<ShoppingListEntity> shoppingList = shoppingListRepository.findByHouseholdId(household.getId());

        if (shoppingList.isEmpty()) {
            ShoppingListEntity entity = new ShoppingListEntity();
            entity.setHousehold(household);
            shoppingListRepository.save(entity);
        }
    }

    @Override
    public ShoppingListDto getShoppingList() {
        Long householdId = currentUserService.getCurrentHousehold().getId();
        ShoppingListEntity shoppingList = getShoppingListEntity(householdId);

        return mapper.toDto(shoppingList);
    }

    @Override
    public ShoppingListDto update(ShoppingListUpdateRequest updateRequest) {
        Long householdId = currentUserService.getCurrentHousehold().getId();
        ShoppingListEntity shoppingList = getShoppingListEntity(householdId);

        updateEntities(shoppingList, updateRequest);

        ShoppingListEntity saved = shoppingListRepository.save(shoppingList);
        return mapper.toDto(saved);
    }

    @Override
    public ShoppingListDto addMealPlan(MealPlanWeek week) {
        Long householdId = currentUserService.getCurrentHousehold().getId();

        ShoppingListEntity shoppingList = getShoppingListEntity(householdId);
        MealPlanEntity mealPlan = mealPlanService.getMealPlanEntity(week);

        List<ShoppingItemEntity> allItems = mergeShoppingListWithMealPlan(mealPlan, shoppingList);
        List<ShoppingItemEntity> aggregated = ShoppingItemAggregator.aggregate(allItems);

        aggregated.forEach(item -> item.setShoppingList(shoppingList));

        shoppingList.getShoppingItems().clear();
        shoppingList.setShoppingItems(aggregated);

        ShoppingListEntity savedShoppingList = shoppingListRepository.save(shoppingList);
        return mapper.toDto(savedShoppingList);
    }

    private List<ShoppingItemEntity> mergeShoppingListWithMealPlan(MealPlanEntity mealPlan, ShoppingListEntity shoppingList) {
        List<ShoppingItemEntity> itemsToAdd = mealPlan.getMealSlots().stream()
                .flatMap(slot -> slot.getRecipe().getIngredients().stream())
                .map(this::mapRecipeIngredient)
                .toList();

        List<ShoppingItemEntity> allItems = new ArrayList<>(shoppingList.getShoppingItems());
        allItems.addAll(itemsToAdd);
        return allItems;
    }

    private ShoppingListEntity getShoppingListEntity(Long householdId) {
        return shoppingListRepository.findByHouseholdId(householdId)
                .orElseThrow(() -> new ServiceException(ErrorCode.SHOPPING_LIST_NOT_FOUND.getTemplate(), ErrorCode.SHOPPING_LIST_NOT_FOUND));
    }

    private void updateEntities(ShoppingListEntity shoppingList, ShoppingListUpdateRequest updateRequest) {
        shoppingList.getShoppingItems().clear();

        List<ShoppingItemEntity> itemsToSave = updateRequest.getShoppingItems().stream()
                .map(item -> createEntity(shoppingList, item))
                .collect(Collectors.toList());

        shoppingList.setShoppingItems(ShoppingItemAggregator.aggregate(itemsToSave));
    }

    private ShoppingItemEntity createEntity(ShoppingListEntity shoppingListEntity, ShoppingItemUpdateRequest item) {
        ShoppingItemEntity entity = new ShoppingItemEntity();
        entity.setNote(item.getNote());
        entity.setShoppingList(shoppingListEntity);
        entity.setChecked(item.isChecked());

        if (isIngredientBased(item)) {
            entity.setIngredient(getIngredient(item.getIngredientId()));
            entity.setQuantity(item.getQuantity());
            entity.setQuantitativeMeasurement(item.getQuantitativeMeasurement());
        } else {
            entity.setName(item.getName());
        }
        return entity;
    }

    private boolean isIngredientBased(ShoppingItemUpdateRequest item) {
        return item.getIngredientId() != null && item.getName() == null;
    }

    private IngredientEntity getIngredient(Long ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ServiceException(ErrorCode.INGREDIENT_NOT_FOUND.format(ingredientId), ErrorCode.INGREDIENT_NOT_FOUND));
    }

    private ShoppingItemEntity mapRecipeIngredient(RecipeIngredientEntity recipeIngredient) {
        ShoppingItemEntity item = new ShoppingItemEntity();
        item.setIngredient(recipeIngredient.getIngredient());

        if (AggregationUtil.isAggregatable(recipeIngredient.getQuantity(), recipeIngredient.getQuantitativeMeasurement())) {
            item.setQuantity(recipeIngredient.getQuantity());
            item.setQuantitativeMeasurement(recipeIngredient.getQuantitativeMeasurement());
        }

        return item;
    }

}
