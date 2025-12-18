package com.farkas.familymealmate.model.dto.mealplan;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record MealPlanUpdateRequest(
        @NotEmpty(message = "{mealplan.slots.notempty}")
        List<@Valid MealSlotUpdateRequest> mealSlots
) {
}
