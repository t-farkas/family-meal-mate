package com.farkas.familymealmate.model.dto.mealplan;

import java.util.List;

public record MealPlanDetailsDto(
        List<MealSlotDetailsDto> mealSlots,
        Long version) {
}
