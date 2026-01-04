package com.farkas.familymealmate.testdata.mealplan;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.dto.mealplan.MealSlotUpdateRequest;
import com.farkas.familymealmate.model.enums.MealPlanWeek;

import java.util.List;

public record TestMealPlan(
        MealPlanWeek week,
        List<TestMealSlot> slots
) {

    public MealPlanUpdateRequest updateRequest() {
        List<MealSlotUpdateRequest> slotUpdateRequests = slots.stream()
                .map(slot -> new MealSlotUpdateRequest(
                        slot.id(),
                        slot.note(),
                        slot.day(),
                        slot.type(),
                        slot.recipeId())).toList();

        return new MealPlanUpdateRequest(week, slotUpdateRequests);
    }
}
