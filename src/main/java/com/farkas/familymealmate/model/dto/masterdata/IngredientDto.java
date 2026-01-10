package com.farkas.familymealmate.model.dto.masterdata;

import com.farkas.familymealmate.model.enums.AllergyType;
import com.farkas.familymealmate.model.enums.IngredientCategory;

import java.util.Set;

public record IngredientDto(
        Long id,
        String name,
        IngredientCategory category,
        Set<AllergyType> allergies
) {
}
