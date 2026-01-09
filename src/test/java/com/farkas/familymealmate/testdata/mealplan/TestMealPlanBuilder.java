package com.farkas.familymealmate.testdata.mealplan;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.model.enums.MealType;
import com.farkas.familymealmate.testdata.recipe.TestRecipe;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class TestMealPlanBuilder {

    private final List<TestMealSlot> mealSlots = new ArrayList<>();
    private MealPlanWeek week;
    private Long version = 0L;

    public TestMealPlanBuilder forWeek(MealPlanWeek week) {
        this.week = week;
        return this;
    }

    public TestMealPlanBuilder version(Long version) {
        this.version = version;
        return this;
    }

    public TestMealPlanBuilder slot(String note, DayOfWeek day, MealType mealType, RecipeEntity recipe) {
        TestMealSlot slot = new TestMealSlot(null, note, day, mealType, new TestRecipe(recipe));
        mealSlots.add(slot);

        return this;
    }

    public MealPlanUpdateRequest buildRequest() {
        TestMealPlan plan = new TestMealPlan(version, week, mealSlots);
        MealPlanUpdateRequest request = plan.updateRequest();
        reset();
        return request;
    }

    private void reset() {
        this.mealSlots.clear();
        this.week = null;
        this.version = 0L;
    }

}
