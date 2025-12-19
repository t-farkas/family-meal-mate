package com.farkas.familymealmate.model.dto.template;

import com.farkas.familymealmate.model.dto.mealplan.MealSlotDetailsDto;

import java.util.List;

public record TemplateDto(Long id, String templateName, List<MealSlotDetailsDto> mealSlots) {
}
