package com.farkas.familymealmate.model.dto.shoppinglist;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingListDto {

    private String note;
    private List<ShoppingItemDto> shoppingItems;

}
