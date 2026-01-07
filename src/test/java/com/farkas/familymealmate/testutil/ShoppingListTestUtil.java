package com.farkas.familymealmate.testutil;

import com.farkas.familymealmate.model.entity.ShoppingItemEntity;
import com.farkas.familymealmate.model.entity.ShoppingListEntity;

public class ShoppingListTestUtil {

    public static ShoppingItemEntity getItemByIngredientId(ShoppingListEntity list, Long id) {
        return list.getShoppingItems().stream()
                .filter(item -> item.getIngredient() != null && item.getIngredient().getId().equals(id))
                .findFirst().orElse(null);
    }

    public static ShoppingItemEntity getItemByIngredientName(ShoppingListEntity list, String name) {
        return list.getShoppingItems().stream()
                .filter(item -> item.getName().equals(name))
                .findFirst().orElse(null);
    }
}
