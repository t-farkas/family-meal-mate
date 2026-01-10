package com.farkas.familymealmate.testutil;

import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingItemDto;
import com.farkas.familymealmate.model.entity.ShoppingItemEntity;
import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;

import java.util.List;

public class ShoppingListTestUtil {

    public static ShoppingItemEntity getEntity(List<ShoppingItemEntity> list, Long id, QuantitativeMeasurement unit) {
        return list.stream()
                .filter(item -> item.getIngredient() != null
                        && item.getIngredient().getId().equals(id)
                        && unit.equals(item.getQuantitativeMeasurement()))
                .findFirst()
                .orElse(null);
    }

    public static ShoppingItemEntity getEntity(List<ShoppingItemEntity> list, Long id) {
        return list.stream()
                .filter(item -> item.getIngredient() != null
                        && item.getIngredient().getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static ShoppingItemEntity getEntityWithNullUnit(List<ShoppingItemEntity> list, Long id) {
        return list.stream()
                .filter(item -> item.getIngredient() != null
                        && item.getIngredient().getId().equals(id)
                        && item.getQuantitativeMeasurement() == null)
                .findFirst()
                .orElse(null);
    }

    public static ShoppingItemDto getDto(List<ShoppingItemDto> list, Long id, QuantitativeMeasurement unit) {
        return list.stream()
                .filter(item -> item.getIngredientId() != null
                        && item.getIngredientId().equals(id)
                        && unit.equals(item.getQuantitativeMeasurement()))
                .findFirst()
                .orElse(null);
    }

    public static ShoppingItemDto getDto(List<ShoppingItemDto> list, Long id) {
        return list.stream()
                .filter(item -> item.getIngredientId() != null
                        && item.getIngredientId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static ShoppingItemDto getDtoWithNullUnit(List<ShoppingItemDto> list, Long id) {
        return list.stream()
                .filter(item -> item.getIngredientId() != null
                        && item.getIngredientId().equals(id)
                        && item.getQuantitativeMeasurement() == null)
                .findFirst()
                .orElse(null);
    }

    public static ShoppingItemDto getItemByName(List<ShoppingItemDto> list, String name) {
        return list.stream()
                .filter(item -> name.equals(item.getName()))
                .findFirst().orElse(null);
    }
}
