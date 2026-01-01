package com.farkas.familymealmate.model.dto.shoppinglist;

import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingItemUpdateRequest {

    private Long id;
    @NotBlank(message = "{shoppingitem.name.notblank}")
    private String name;
    private String note;
    private boolean checked;
    private Long ingredientId;
    private BigDecimal quantity;
    private QuantitativeMeasurement quantitativeMeasurement;

}
