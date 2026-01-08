package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.model.entity.MealPlanEntity;
import com.farkas.familymealmate.model.entity.RecipeIngredientEntity;
import com.farkas.familymealmate.model.entity.ShoppingItemEntity;
import com.farkas.familymealmate.model.entity.ShoppingListEntity;
import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;
import com.farkas.familymealmate.util.UnitConversionUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShoppingListAggregator {

    public void aggregate(ShoppingListEntity shoppingList, MealPlanEntity mealPlan) {

        Map<Long, ShoppingItemEntity> itemMap = shoppingList.getShoppingItems().stream()
                .filter(item -> item.getIngredient() != null)
                .collect(Collectors.toMap(
                        item -> item.getIngredient().getId(),
                        item -> item));

        List<RecipeIngredientEntity> ingredients = mealPlan.getMealSlots()
                .stream()
                .flatMap(slot -> slot.getRecipe().getIngredients().stream()).toList();

        ingredients.forEach(
                ingredient -> {
                    ShoppingItemEntity existingItem = itemMap.get(ingredient.getIngredient().getId());

                    if (existingItem == null) {
                        ShoppingItemEntity item = addNewItem(shoppingList, ingredient);
                        itemMap.put(ingredient.getIngredient().getId(), item);
                    } else {
                        if (isAggregatable(ingredient)) {
                            if (canConvert(ingredient, existingItem)) {
                                aggregateQuantities(ingredient, existingItem);
                            } else {
                                ShoppingItemEntity item = addNewItem(shoppingList, ingredient);
                                itemMap.put(ingredient.getIngredient().getId(), item);

                            }
                        }
                    }
                });
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

    private void aggregateQuantities(RecipeIngredientEntity ingredient, ShoppingItemEntity item) {
        BigDecimal value = ingredient.getQuantity();
        QuantitativeMeasurement from = ingredient.getQuantitativeMeasurement();
        QuantitativeMeasurement to = item.getQuantitativeMeasurement();

        if (to == null){
            item.setQuantity(value);
            item.setQuantitativeMeasurement(from);
        }else {

            BigDecimal convertedValue = UnitConversionUtil.convert(value, from, to);
            item.setQuantity(item.getQuantity().add(convertedValue));
        }

    }

    private boolean isAggregatable(RecipeIngredientEntity ingredient) {
        return isQuantitative(ingredient) && UnitConversionUtil.shouldKeepQuantity(ingredient.getQuantitativeMeasurement());
    }

    private boolean canConvert(RecipeIngredientEntity ingredient, ShoppingItemEntity item) {
        return UnitConversionUtil.canConvert(ingredient.getQuantitativeMeasurement(), item.getQuantitativeMeasurement());
    }

    private boolean isQuantitative(RecipeIngredientEntity ingredient) {
        return ingredient.getQuantity() != null && ingredient.getQuantitativeMeasurement() != null;
    }
}
