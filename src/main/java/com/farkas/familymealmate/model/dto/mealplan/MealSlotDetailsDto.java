package com.farkas.familymealmate.model.dto.mealplan;

import java.time.LocalDate;

public record MealSlotDetailsDto(Long recipeId, String recipeTitle, String note, LocalDate date) {
}
