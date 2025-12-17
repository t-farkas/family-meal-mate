package com.farkas.familymealmate.model.dto.recipe.ingredient;

import com.farkas.familymealmate.model.enums.QualitativeMeasurement;
import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredientCreateRequestDto {

    @NotNull(message = "{recipeIngredient.ingredientId.notnull}")
    private Long ingredientId;
    private BigDecimal quantity;
    private QuantitativeMeasurement quantitativeMeasurement;
    private QualitativeMeasurement qualitativeMeasurement;
}
