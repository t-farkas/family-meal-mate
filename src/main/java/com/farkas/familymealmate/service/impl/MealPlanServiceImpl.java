package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.MealPlanEntity;
import com.farkas.familymealmate.repository.MealPlanRepository;
import com.farkas.familymealmate.security.CurrentUserService;
import com.farkas.familymealmate.service.MealPlanService;
import com.farkas.familymealmate.util.MealPlanDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MealPlanServiceImpl implements MealPlanService {

    private final CurrentUserService currentUserService;
    private final MealPlanRepository mealPlanRepository;

    @Override
    public void createMealPlans() {
        HouseholdEntity currentHousehold = currentUserService.getCurrentHousehold();

        LocalDate currentWeekStart = MealPlanDateUtils.getCurrentWeekStart();
        Optional<MealPlanEntity> currentWeekMealPlan = getMealPlan(currentHousehold, currentWeekStart);
        if (currentWeekMealPlan.isEmpty()){
            saveMealPlan(currentWeekStart, currentHousehold);
        }

        LocalDate nextWeekStart = MealPlanDateUtils.getNextWeekStart();
        Optional<MealPlanEntity> nextWeekMealPlan = getMealPlan(currentHousehold, nextWeekStart);
        if (nextWeekMealPlan.isEmpty()){
            saveMealPlan(nextWeekStart, currentHousehold);
        }

    }

    private Optional<MealPlanEntity> getMealPlan(HouseholdEntity currentHousehold, LocalDate currentWeekStart) {
        return mealPlanRepository.findByHouseholdIdAndWeekStart(currentHousehold.getId(), currentWeekStart);
    }

    private void saveMealPlan(LocalDate currentWeekStart, HouseholdEntity currentHousehold) {
        MealPlanEntity mealPlanEntity = new MealPlanEntity();
        mealPlanEntity.setWeekStart(currentWeekStart);
        mealPlanEntity.setHousehold(currentHousehold);
        mealPlanEntity.setTemplate(false);

        mealPlanRepository.save(mealPlanEntity);
    }
}
