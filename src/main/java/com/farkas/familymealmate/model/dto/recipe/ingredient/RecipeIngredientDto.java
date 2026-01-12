package com.farkas.familymealmate.model.dto.recipe.ingredient;

import com.farkas.familymealmate.model.enums.Measurement;
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
    private Measurement measurement;
}
