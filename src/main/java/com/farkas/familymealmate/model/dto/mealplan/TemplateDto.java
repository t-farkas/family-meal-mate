package com.farkas.familymealmate.model.dto.mealplan;

import java.util.List;

public record TemplateDto(Long id, List<MealSlotDetailsDto> mealSlots) {
}
