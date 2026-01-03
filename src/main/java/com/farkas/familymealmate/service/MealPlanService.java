package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.entity.MealPlanEntity;
import com.farkas.familymealmate.model.enums.MealPlanWeek;

public interface MealPlanService {
    void createMealPlans();

    MealPlanDetailsDto getMealPlan(MealPlanWeek week);

    MealPlanEntity getMealPlanEntity(MealPlanWeek week);

    MealPlanDetailsDto editMealPlan(MealPlanUpdateRequest mealPlanRequest);

}
