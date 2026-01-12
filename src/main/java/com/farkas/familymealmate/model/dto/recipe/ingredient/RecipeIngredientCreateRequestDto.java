package com.farkas.familymealmate.model.dto.recipe.ingredient;

import com.farkas.familymealmate.model.enums.Measurement;
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
    @NotNull(message = "recipeIngredient.measurement.notnull}")
    private Measurement measurement;
}
