package com.farkas.familymealmate.testdata.mealplan;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.dto.mealplan.MealSlotUpdateRequest;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.model.enums.MealType;
import com.farkas.familymealmate.service.MealPlanService;
import com.farkas.familymealmate.testdata.recipe.TestRecipe;
import com.farkas.familymealmate.testdata.recipe.TestRecipeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestMealPlanHelper {

    private final List<MealSlotUpdateRequest> slots = new ArrayList<>();
    @Autowired
    private TestRecipeFactory recipeFactory;
    @Autowired
    private MealPlanService mealPlanService;
    private MealPlanWeek week;

    public TestMealPlanHelper forWeek(MealPlanWeek week) {
        this.week = week;
        return this;
    }

    public TestMealPlanHelper slot(String note, DayOfWeek day, MealType mealType, TestRecipe recipe) {
        slot(null, note, day, mealType, recipe);
        return this;
    }

    public TestMealPlanHelper slot(Long id, String note, DayOfWeek day, MealType mealType, TestRecipe recipe) {
        Long recipeId = this.recipeFactory.createRecipe(recipe);
        MealSlotUpdateRequest slot = new MealSlotUpdateRequest(id, note, day, mealType, recipeId);

        slots.add(slot);
        return this;
    }

    public MealPlanDetailsDto persist() {
        mealPlanService.createMealPlans();
        MealPlanDetailsDto mealPlanDetailsDto = mealPlanService.editMealPlan(new MealPlanUpdateRequest(week, slots));
        reset();
        return mealPlanDetailsDto;
    }

    private void reset() {
        this.slots.clear();
        this.week = null;
    }

}
