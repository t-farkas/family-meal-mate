package com.farkas.familymealmate.model.dto.shoppinglist;

import com.farkas.familymealmate.model.enums.IngredientCategory;
import com.farkas.familymealmate.model.enums.Measurement;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingItemDto {

    private Long id;
    private String name;
    private String note;
    private boolean checked;
    private Long ingredientId;
    private BigDecimal quantity;
    private Measurement measurement;
    private IngredientCategory category;

}
