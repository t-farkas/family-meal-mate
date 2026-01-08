package com.farkas.familymealmate.testutil;

import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingItemDto;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListDto;
import com.farkas.familymealmate.model.entity.ShoppingItemEntity;
import com.farkas.familymealmate.model.entity.ShoppingListEntity;
import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;

import java.util.List;

public class ShoppingListTestUtil {

    public static ShoppingItemEntity getFirstItemByIngredientId(ShoppingListEntity list, Long id) {
        return list.getShoppingItems().stream()
                .filter(item -> item.getIngredient() != null && item.getIngredient().getId().equals(id))
                .findFirst().orElse(null);
    }

    public static List<ShoppingItemEntity> getItemListByIngredientId(ShoppingListEntity list, Long id) {
        return list.getShoppingItems().stream()
                .filter(item -> item.getIngredient() != null && item.getIngredient().getId().equals(id))
                .toList();
    }

    public static ShoppingItemDto getFirstItemByIngredientId(ShoppingListDto list, Long id) {
        return list.getShoppingItems().stream()
                .filter(item -> item.getIngredientId() != null && item.getIngredientId().equals(id))
                .findFirst().orElse(null);
    }

    public static ShoppingItemDto getItemByName(ShoppingListDto list, String name) {
        return list.getShoppingItems().stream()
                .filter(item -> item.getName().equals(name))
                .findFirst().orElse(null);
    }

    public static ShoppingItemEntity getByMeasurement(List<ShoppingItemEntity> list, QuantitativeMeasurement measurement) {
        return list.stream()
                .filter(item -> item.getQuantitativeMeasurement() != null && item.getQuantitativeMeasurement().equals(measurement))
                .findFirst().orElse(null);
    }
}
