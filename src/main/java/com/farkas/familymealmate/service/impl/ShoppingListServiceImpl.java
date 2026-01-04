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
import com.farkas.familymealmate.util.UnitConversionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
    public ShoppingListDto edit(ShoppingListUpdateRequest updateRequest) {
        Long householdId = currentUserService.getCurrentHousehold().getId();
        ShoppingListEntity shoppingList = getShoppingListEntity(householdId);

        return editShoppingList(updateRequest, shoppingList);
    }

    @Override
    public ShoppingListDto addMealPlan(MealPlanWeek week) {
        Long householdId = currentUserService.getCurrentHousehold().getId();
        ShoppingListEntity shoppingList = getShoppingListEntity(householdId);
        Map<Long, ShoppingItemEntity> itemMap = shoppingList.getShoppingItems().stream()
                .collect(Collectors.toMap(
                        item -> item.getIngredient().getId(),
                        item -> item));

        MealPlanEntity mealPlan = mealPlanService.getMealPlanEntity(week);
        List<RecipeIngredientEntity> ingredients = mealPlan.getMealSlots()
                .stream()
                .flatMap(slot -> slot.getRecipe().getIngredients().stream()).toList();

        ingredients.forEach(
                ingredient -> {
                    ShoppingItemEntity existingItem = itemMap.get(ingredient.getId());

                    if (existingItem == null) {
                        ShoppingItemEntity item = addNewItem(shoppingList, ingredient);
                        itemMap.put(ingredient.getIngredient().getId(), item);
                    } else {
                        if (isAggregatable(ingredient)) {
                            if (canConvert(ingredient, existingItem)) {
                                aggregate(ingredient, existingItem);
                            } else {
                                ShoppingItemEntity item = addNewItem(shoppingList, ingredient);
                                itemMap.put(ingredient.getIngredient().getId(), item);

                            }
                        }
                    }
                });

        ShoppingListEntity savedShoppingList = shoppingListRepository.save(shoppingList);
        return mapper.toDto(savedShoppingList);

    }

    private void aggregate(RecipeIngredientEntity ingredient, ShoppingItemEntity item) {
        BigDecimal convertedValue = UnitConversionUtil.convert(
                ingredient.getQuantity(),
                ingredient.getQuantitativeMeasurement(),
                item.getQuantitativeMeasurement());

        item.setQuantity(item.getQuantity().add(convertedValue));
    }

    private boolean canConvert(RecipeIngredientEntity ingredient, ShoppingItemEntity item) {
        return UnitConversionUtil.canConvert(ingredient.getQuantitativeMeasurement(), item.getQuantitativeMeasurement());
    }

    private boolean isAggregatable(RecipeIngredientEntity ingredient) {
        return isQuantitative(ingredient) && UnitConversionUtil.shouldKeepQuantity(ingredient.getQuantitativeMeasurement());
    }

    private ShoppingItemEntity addNewItem(ShoppingListEntity shoppingList, RecipeIngredientEntity ingredient) {
        ShoppingItemEntity item = new ShoppingItemEntity();
        item.setIngredient(ingredient.getIngredient());

        if (isAggregatable(ingredient)) {
            item.setQuantity(ingredient.getQuantity());
            item.setQuantitativeMeasurement(ingredient.getQuantitativeMeasurement());
        }

        item.setShoppingList(shoppingList);
        shoppingList.getShoppingItems().add(item);

        return item;
    }

    private ShoppingListEntity getShoppingListEntity(Long householdId) {
        return shoppingListRepository.findByHouseholdId(householdId)
                .orElseThrow(() -> new ServiceException(ErrorCode.SHOPPING_LIST_NOT_FOUND.getTemplate(), ErrorCode.SHOPPING_LIST_NOT_FOUND));
    }

    private ShoppingListDto editShoppingList(ShoppingListUpdateRequest updateRequest, ShoppingListEntity shoppingListEntity) {
        List<ShoppingItemEntity> shoppingItemEntities = shoppingListEntity.getShoppingItems();
        List<ShoppingItemUpdateRequest> shoppingItems = updateRequest.getShoppingItems();

        deleteRemoved(shoppingItemEntities, shoppingItems);
        updateExisting(shoppingItemEntities, shoppingItems);
        shoppingItemEntities.addAll(createNew(shoppingListEntity, shoppingItems));

        ShoppingListEntity saved = shoppingListRepository.save(shoppingListEntity);
        return mapper.toDto(saved);
    }

    private List<ShoppingItemEntity> createNew(ShoppingListEntity shoppingListEntity, List<ShoppingItemUpdateRequest> shoppingItems) {
        return shoppingItems.stream()
                .filter(item -> item.getId() == null)
                .map(item -> {
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
                }).collect(Collectors.toList());

    }

    private void updateExisting(List<ShoppingItemEntity> shoppingItemEntites, List<ShoppingItemUpdateRequest> shoppingItems) {
        Map<Long, ShoppingItemEntity> entityMap = shoppingItemEntites.stream()
                .collect(Collectors.toMap(BaseEntity::getId, entity -> entity));

        shoppingItems.stream()
                .filter(item -> item.getId() != null)
                .forEach(item -> {
                    ShoppingItemEntity entity = entityMap.get(item.getId());
                    nullCheckEntity(item, entity);

                    entity.setNote(item.getNote());
                    entity.setChecked(item.isChecked());

                    if (isIngredientBased(item)) {
                        entity.setQuantity(item.getQuantity());
                        entity.setQuantitativeMeasurement(item.getQuantitativeMeasurement());
                    } else {
                        entity.setName(item.getName());
                    }
                });

    }

    private void nullCheckEntity(ShoppingItemUpdateRequest item, ShoppingItemEntity entity) {
        if (entity == null) {
            throw new ServiceException(
                    ErrorCode.SHOPPING_ITEM_NOT_FOUND.format(item.getId()),
                    ErrorCode.SHOPPING_ITEM_NOT_FOUND);
        }
    }

    private boolean isIngredientBased(ShoppingItemUpdateRequest item) {
        return item.getIngredientId() != null && item.getName() == null;
    }

    private IngredientEntity getIngredient(Long ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ServiceException(ErrorCode.INGREDIENT_NOT_FOUND.format(ingredientId), ErrorCode.INGREDIENT_NOT_FOUND));
    }

    private void deleteRemoved(List<ShoppingItemEntity> shoppingItemEntities, List<ShoppingItemUpdateRequest> shoppingItems) {
        List<Long> requestIds = shoppingItems.stream().map(ShoppingItemUpdateRequest::getId).toList();
        shoppingItemEntities.removeIf(entity -> !requestIds.contains(entity.getId()));
    }

    private boolean isQuantitative(RecipeIngredientEntity ingredient) {
        return ingredient.getQuantity() != null && ingredient.getQuantitativeMeasurement() != null;
    }
}
