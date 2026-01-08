package com.farkas.familymealmate.model.dto.shoppinglist;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingListUpdateRequest {

    private String note;
    @NotNull(message = "{shoppinglist.version.notnull}")
    Long version;
    @NotEmpty(message = "{shoppinglist.items.notempty}")
    private List<@Valid ShoppingItemUpdateRequest> shoppingItems;

}
