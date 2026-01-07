package com.farkas.familymealmate.testdata.mealplan;

import com.farkas.familymealmate.model.enums.MealType;
import com.farkas.familymealmate.testdata.recipe.TestRecipe;

import java.time.DayOfWeek;

public record TestMealSlot(
        Long id,
        String note,
        DayOfWeek day,
        MealType type,
        TestRecipe recipe
) {
}
