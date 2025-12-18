package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;

public interface MealPlanService {
    void createMealPlans();

    MealPlanDetailsDto getCurrentWeek();

    MealPlanDetailsDto getNextWeek();

    MealPlanDetailsDto editCurrentWeek(MealPlanUpdateRequest mealPlanRequest);

    MealPlanDetailsDto editNextWeek(MealPlanUpdateRequest mealPlanRequest);
}
