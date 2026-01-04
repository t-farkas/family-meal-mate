package com.farkas.familymealmate.testdata.mealplan;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.model.enums.MealType;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class TestMealPlanBuilder {

    private List<TestMealSlot> mealSlots = new ArrayList<>();
    private MealPlanWeek week;

    public TestMealPlanBuilder forWeek(MealPlanWeek week) {
        this.week = week;
        return this;
    }

    public TestMealPlanBuilder slot(String note, DayOfWeek day, MealType mealType, Long recipeId) {
        slot(null, note, day, mealType, recipeId);
        return this;
    }

    public TestMealPlanBuilder slot(Long id, String note, DayOfWeek day, MealType mealType, Long recipeId) {
        TestMealSlot slot = new TestMealSlot(id, note, day, mealType, recipeId);

        mealSlots.add(slot);
        return this;
    }

    public MealPlanUpdateRequest build() {
        TestMealPlan plan = new TestMealPlan(week, mealSlots);
        MealPlanUpdateRequest request = plan.updateRequest();
        reset();
        return request;
    }

    private void reset() {
        this.mealSlots = new ArrayList<>();
        this.week = null;

    }

}
