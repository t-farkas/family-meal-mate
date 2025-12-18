package com.farkas.familymealmate.model.dto.mealplan;

import com.farkas.familymealmate.model.enums.MealType;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;

public record MealSlotUpdateRequest(
        Long id,
        String note,
        @NotNull(message = "{mealslot.day.notnull}")
        DayOfWeek day,
        @NotNull(message = "{mealslot.mealtype.notnull}")
        MealType mealType,
        @NotNull(message = "{mealslot.recipeid.notnull}")
        Long recipeId
) {
}
