package com.farkas.familymealmate.model.dto.mealplan;

import java.util.List;

public record MealPlanDetailsDto(boolean favourite, List<MealSlotDetailsDto> mealSlots) {
}
