package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.VersionDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.MealPlanEntity;
import com.farkas.familymealmate.model.enums.MealPlanWeek;

public interface MealPlanService {

    void create(HouseholdEntity houseHold);

    MealPlanDetailsDto get(MealPlanWeek week);

    MealPlanEntity getEntity(MealPlanWeek week);

    MealPlanDetailsDto update(MealPlanUpdateRequest mealPlanRequest);

    VersionDto getVersion(MealPlanWeek week);

}
