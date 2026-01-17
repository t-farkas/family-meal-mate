package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.mapper.MealPlanMapper;
import com.farkas.familymealmate.model.dto.VersionDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.dto.mealplan.MealSlotUpdateRequest;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.MealPlanEntity;
import com.farkas.familymealmate.model.entity.MealSlotEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.repository.MealPlanRepository;
import com.farkas.familymealmate.security.CurrentUserHelper;
import com.farkas.familymealmate.service.MealPlanService;
import com.farkas.familymealmate.service.RecipeService;
import com.farkas.familymealmate.util.MealPlanDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MealPlanServiceImpl implements MealPlanService {

    private final MealPlanRepository mealPlanRepository;
    private final MealPlanMapper mealPlanMapper;
    private final RecipeService recipeService;

    @Override
    public void create(HouseholdEntity household) {
        LocalDate currentWeekStart = MealPlanDateUtils.getCurrentWeekStart();
        if (!mealPlanRepository.existsByHouseholdIdAndWeekStart(household.getId(), currentWeekStart)) {
            saveMealPlan(currentWeekStart, household);
        }

        LocalDate nextWeekStart = MealPlanDateUtils.getNextWeekStart();
        if (!mealPlanRepository.existsByHouseholdIdAndWeekStart(household.getId(), nextWeekStart)) {
            saveMealPlan(nextWeekStart, household);
        }
    }

    @Override
    public MealPlanDetailsDto get(MealPlanWeek week) {
        HouseholdEntity household = CurrentUserHelper.getCurrentHousehold();
        LocalDate weekStart = getWeekStart(week);
        MealPlanEntity mealPlan = getMealPlanEntity(household, weekStart);

        return mealPlanMapper.toDto(mealPlan);
    }

    @Override
    public void cleanupBefore(LocalDate before) {
        mealPlanRepository.deleteByWeekStartBefore(before);
    }

    @Override
    public MealPlanDetailsDto update(MealPlanUpdateRequest updateRequest) {
        HouseholdEntity household = CurrentUserHelper.getCurrentHousehold();
        LocalDate weekStart = getWeekStart(updateRequest.week());
        MealPlanEntity mealPlanEntity = getMealPlanEntity(household, weekStart);

        MealPlanEntity entity = getEditedMealPlanEntity(updateRequest, mealPlanEntity);

        try {
            MealPlanEntity saved = mealPlanRepository.save(entity);
            return mealPlanMapper.toDto(saved);
        } catch (ObjectOptimisticLockingFailureException exception) {
            throw new ServiceException(ErrorCode.MEAL_PLAN_VERSION_MISMATCH);
        }
    }

    private MealPlanEntity getEditedMealPlanEntity(MealPlanUpdateRequest updateRequest, MealPlanEntity mealPlanEntity) {
        MealPlanEntity entity = new MealPlanEntity();
        entity.setId(mealPlanEntity.getId());
        entity.setHousehold(mealPlanEntity.getHousehold());
        entity.setWeekStart(mealPlanEntity.getWeekStart());
        entity.getMealSlots().addAll(mapMealSlots(updateRequest, entity));
        entity.setVersion(updateRequest.version());
        return entity;
    }

    @Override
    public MealPlanEntity getEntity(MealPlanWeek week) {
        HouseholdEntity household = CurrentUserHelper.getCurrentHousehold();
        LocalDate weekStart = getWeekStart(week);
        return getMealPlanEntity(household, weekStart);
    }

    @Override
    public VersionDto getVersion(MealPlanWeek week) {
        MealPlanEntity mealPlan = getEntity(week);
        return new VersionDto(mealPlan.getVersion());
    }

    private LocalDate getWeekStart(MealPlanWeek week) {
        return switch (week) {
            case CURRENT -> MealPlanDateUtils.getCurrentWeekStart();
            case NEXT -> MealPlanDateUtils.getNextWeekStart();
        };
    }

    private List<MealSlotEntity> mapMealSlots(MealPlanUpdateRequest mealPlanRequest, MealPlanEntity mealPlanEntity) {
        return mealPlanRequest.mealSlots().stream()
                .map(slot -> createEntity(slot, mealPlanEntity))
                .toList();
    }

    private MealPlanEntity getMealPlanEntity(HouseholdEntity household, LocalDate weekStart) {
        return mealPlanRepository.findByHouseholdIdAndWeekStart(household.getId(), weekStart)
                .orElseThrow(() -> new ServiceException(ErrorCode.MEAL_PLAN_NOT_FOUND.format("current"), ErrorCode.MEAL_PLAN_NOT_FOUND));
    }

    private void saveMealPlan(LocalDate currentWeekStart, HouseholdEntity currentHousehold) {
        MealPlanEntity mealPlanEntity = new MealPlanEntity();
        mealPlanEntity.setWeekStart(currentWeekStart);
        mealPlanEntity.setHousehold(currentHousehold);
        mealPlanEntity.setTemplate(false);

        mealPlanRepository.save(mealPlanEntity);
    }

    private MealSlotEntity createEntity(MealSlotUpdateRequest slot, MealPlanEntity mealPlan) {
        MealSlotEntity entity = new MealSlotEntity();
        entity.setMealPlan(mealPlan);
        entity.setMealType(slot.mealType());
        entity.setDay(slot.day());
        entity.setNote(slot.note());
        entity.setRecipe(recipeService.getEntity(slot.recipeId()));

        return entity;
    }
}
