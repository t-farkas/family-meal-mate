package com.farkas.familymealmate.model.dto.mealplan;

import com.farkas.familymealmate.model.enums.MealPlanWeek;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MealPlanUpdateRequest(
        @NotNull(message = "{mealplan.week.notnull}")
        MealPlanWeek week,
        @NotNull(message = "{mealplan.version.notnull}")
        Long version,
        @NotEmpty(message = "{mealplan.slots.notempty}")
        List<@Valid MealSlotUpdateRequest> mealSlots
) {
}
