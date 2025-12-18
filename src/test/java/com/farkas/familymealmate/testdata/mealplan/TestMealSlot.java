package com.farkas.familymealmate.testdata.mealplan;

import com.farkas.familymealmate.model.dto.mealplan.MealSlotUpdateRequest;
import com.farkas.familymealmate.model.enums.MealType;
import com.farkas.familymealmate.util.MealPlanDateUtils;

public record TestMealSlot(Long id, String note, Long recipeId) {

    public MealSlotUpdateRequest lunch() {
        return new MealSlotUpdateRequest(id, note, MealPlanDateUtils.getCurrentWeekStart(), MealType.LUNCH, recipeId);
    }

    public MealSlotUpdateRequest breakfast() {
        return new MealSlotUpdateRequest(id, note, MealPlanDateUtils.getCurrentWeekStart(), MealType.BREAKFAST, recipeId);
    }
}
