package com.farkas.familymealmate.model.dto.template;

import com.farkas.familymealmate.model.enums.MealPlanWeek;

public record TemplateCreateRequest(String name, MealPlanWeek week) {
}
