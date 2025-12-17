package com.farkas.familymealmate.model.dto.recipe.ingredient;

import com.farkas.familymealmate.model.enums.QualitativeMeasurement;
import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeIngredientDto {

    private String ingredientName;
    private BigDecimal quantity;
    private QuantitativeMeasurement quantitativeMeasurement;
    private QualitativeMeasurement qualitativeMeasurement;
}
