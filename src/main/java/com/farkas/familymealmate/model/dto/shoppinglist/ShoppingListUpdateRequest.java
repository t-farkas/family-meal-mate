package com.farkas.familymealmate.model.dto.shoppinglist;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingListUpdateRequest {

    private String note;
    @NotEmpty(message = "{shoppinglist.items.notempty}")
    private List<@Valid ShoppingItemUpdateRequest> shoppingItems;

}
