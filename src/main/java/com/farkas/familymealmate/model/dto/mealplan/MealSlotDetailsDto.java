package com.farkas.familymealmate.model.dto.mealplan;

import com.farkas.familymealmate.model.enums.MealType;

import java.time.DayOfWeek;

public record MealSlotDetailsDto(
        Long id,
        Long recipeId,
        String recipeTitle,
        String note,
        DayOfWeek day,
        MealType mealType) {
}
