package com.farkas.familymealmate.testdata.mealplan;

import com.farkas.familymealmate.model.dto.mealplan.MealSlotUpdateRequest;
import com.farkas.familymealmate.model.enums.MealType;

import java.time.DayOfWeek;

public record TestMealSlot(Long id, String note, Long recipeId) {

    public MealSlotUpdateRequest lunch() {
        return new MealSlotUpdateRequest(id, note, DayOfWeek.MONDAY, MealType.LUNCH, recipeId);
    }

    public MealSlotUpdateRequest breakfast() {
        return new MealSlotUpdateRequest(id, note, DayOfWeek.FRIDAY, MealType.BREAKFAST, recipeId);
    }
}
