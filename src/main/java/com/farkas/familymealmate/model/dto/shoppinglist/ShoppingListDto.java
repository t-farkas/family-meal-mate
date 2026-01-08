package com.farkas.familymealmate.model.dto.shoppinglist;

import java.util.List;

public record ShoppingListDto(
        String note,
        Long version,
        List<ShoppingItemDto> shoppingItems) {
}
