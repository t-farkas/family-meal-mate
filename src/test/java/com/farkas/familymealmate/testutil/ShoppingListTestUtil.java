package com.farkas.familymealmate.testutil;

import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingItemDto;
import com.farkas.familymealmate.model.entity.ShoppingItemEntity;
import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;

import java.util.List;

public class ShoppingListTestUtil {

    public static ShoppingItemEntity getFirstItemByIngredientId(List<ShoppingItemEntity> list, Long id) {
        return list.stream()
                .filter(item -> item.getIngredient() != null && item.getIngredient().getId().equals(id))
                .findFirst().orElse(null);
    }

    public static List<ShoppingItemEntity> getItemListByIngredientId(List<ShoppingItemEntity> list, Long id) {
        return list.stream()
                .filter(item -> item.getIngredient() != null && item.getIngredient().getId().equals(id))
                .toList();
    }

    public static ShoppingItemDto getFirstItemDtoByIngredientId(List<ShoppingItemDto> list, Long id) {
        return list.stream()
                .filter(item -> item.getIngredientId() != null && item.getIngredientId().equals(id))
                .findFirst().orElse(null);
    }

    public static ShoppingItemDto getItemByName(List<ShoppingItemDto> list, String name) {
        return list.stream()
                .filter(item -> name.equals(item.getName()))
                .findFirst().orElse(null);
    }

    public static ShoppingItemEntity getByMeasurement(List<ShoppingItemEntity> list, QuantitativeMeasurement measurement) {
        return list.stream()
                .filter(item -> item.getQuantitativeMeasurement() != null && item.getQuantitativeMeasurement().equals(measurement))
                .findFirst().orElse(null);
    }
}
