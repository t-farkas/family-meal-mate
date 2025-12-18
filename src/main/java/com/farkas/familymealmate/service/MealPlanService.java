package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;

public interface MealPlanService {
    void createMealPlans();
    MealPlanDetailsDto getCurrentWeek();
    MealPlanDetailsDto getNextWeek();
}
