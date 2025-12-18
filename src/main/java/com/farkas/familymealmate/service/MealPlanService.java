package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.enums.MealPlanWeek;

import java.util.List;

public interface MealPlanService {
    void createMealPlans();

    MealPlanDetailsDto getMealPlan(MealPlanWeek week);

    MealPlanDetailsDto editMealPlan(MealPlanUpdateRequest mealPlanRequest);

    void markFavourite(MealPlanWeek week);

    void deleteFavourite(Long id);

    List<MealPlanDetailsDto> listFavourites();
}
