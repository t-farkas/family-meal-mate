package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.mapper.MealPlanMapper;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.MealPlanEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
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
    private final MealPlanMapper mealPlanMapper;

    @Override
    public void createMealPlans() {
        HouseholdEntity currentHousehold = currentUserService.getCurrentHousehold();

        LocalDate currentWeekStart = MealPlanDateUtils.getCurrentWeekStart();
        Optional<MealPlanEntity> currentWeekMealPlan = getMealPlanOptional(currentHousehold, currentWeekStart);
        if (currentWeekMealPlan.isEmpty()) {
            saveMealPlan(currentWeekStart, currentHousehold);
        }

        LocalDate nextWeekStart = MealPlanDateUtils.getNextWeekStart();
        Optional<MealPlanEntity> nextWeekMealPlan = getMealPlanOptional(currentHousehold, nextWeekStart);
        if (nextWeekMealPlan.isEmpty()) {
            saveMealPlan(nextWeekStart, currentHousehold);
        }

    }

    @Override
    public MealPlanDetailsDto getCurrentWeek() {
        HouseholdEntity currentHousehold = currentUserService.getCurrentHousehold();
        LocalDate weekStart = MealPlanDateUtils.getCurrentWeekStart();
        MealPlanEntity mealPlan = getMealPlanEntity(currentHousehold, weekStart);
        return mealPlanMapper.toDto(mealPlan);

    }

    @Override
    public MealPlanDetailsDto getNextWeek() {
        HouseholdEntity currentHousehold = currentUserService.getCurrentHousehold();
        LocalDate weekStart = MealPlanDateUtils.getNextWeekStart();
        MealPlanEntity mealPlan = getMealPlanEntity(currentHousehold, weekStart);
        return mealPlanMapper.toDto(mealPlan);
    }

    private MealPlanEntity getMealPlanEntity(HouseholdEntity currentHousehold, LocalDate weekStart) {
        return mealPlanRepository.findByHouseholdIdAndWeekStart(currentHousehold.getId(), weekStart)
                .orElseThrow(() -> new ServiceException(ErrorCode.MEALPLAN_NOT_FOUND.format("current"), ErrorCode.MEALPLAN_NOT_FOUND));
    }

    private Optional<MealPlanEntity> getMealPlanOptional(HouseholdEntity currentHousehold, LocalDate currentWeekStart) {
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
