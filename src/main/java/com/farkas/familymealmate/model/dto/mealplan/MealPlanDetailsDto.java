package com.farkas.familymealmate.model.dto.mealplan;

import java.util.List;

public record MealPlanDetailsDto(Long id, boolean favourite, List<MealSlotDetailsDto> mealSlots) {
}
