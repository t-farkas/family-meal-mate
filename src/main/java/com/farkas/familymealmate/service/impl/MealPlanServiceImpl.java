package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.mapper.mealplan.MealPlanMapper;
import com.farkas.familymealmate.model.dto.VersionDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.dto.mealplan.MealSlotUpdateRequest;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.MealPlanEntity;
import com.farkas.familymealmate.model.entity.MealSlotEntity;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.repository.MealPlanRepository;
import com.farkas.familymealmate.repository.RecipeRepository;
import com.farkas.familymealmate.security.CurrentUserHelper;
import com.farkas.familymealmate.service.MealPlanService;
import com.farkas.familymealmate.util.MealPlanDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class MealPlanServiceImpl implements MealPlanService {

    private final MealPlanRepository mealPlanRepository;
    private final MealPlanMapper mealPlanMapper;
    private final RecipeRepository recipeRepository;

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
        MealPlanEntity mealPlan = getMealPlanEntityWithMealSlotsAndRecipes(household, weekStart);

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
        MealPlanEntity mealPlan = getMealPlanEntityWithMealSlots(household, weekStart);
        versionCheck(mealPlan, updateRequest.version());

        Map<Long, RecipeEntity> recipesById = getRecipesByIdMap(updateRequest, household);
        updateMealSlots(updateRequest, mealPlan, recipesById);

        try {
            MealPlanEntity saved = mealPlanRepository.save(mealPlan);
            return mealPlanMapper.toDto(saved);
        } catch (ObjectOptimisticLockingFailureException exception) {
            throw new ServiceException(ErrorCode.MEAL_PLAN_VERSION_MISMATCH);
        }
    }

    private Map<Long, RecipeEntity> getRecipesByIdMap(MealPlanUpdateRequest updateRequest, HouseholdEntity household) {
        List<Long> recipeIdList = updateRequest.mealSlots().stream()
                .map(MealSlotUpdateRequest::recipeId)
                .distinct()
                .toList();

        return recipeRepository.findAllByIdInAndHouseholdId(recipeIdList, household.getId())
                .stream()
                .collect(Collectors.toMap(RecipeEntity::getId, Function.identity()));
    }

    private void versionCheck(MealPlanEntity mealPlanEntity, Long version) {
        if (!mealPlanEntity.getVersion().equals(version)) {
            throw new ServiceException(ErrorCode.MEAL_PLAN_VERSION_MISMATCH);
        }
    }

    private void updateMealSlots(MealPlanUpdateRequest updateRequest, MealPlanEntity mealPlan, Map<Long, RecipeEntity> recipesById) {
        mealPlan.getMealSlots().clear();

        mealPlan.getMealSlots().addAll(
                updateRequest.mealSlots().stream()
                        .map(slot -> {
                            RecipeEntity recipeEntity = getRecipeEntity(recipesById, slot.recipeId());
                            return createMealSlot(slot, mealPlan, recipeEntity);
                        })
                        .toList());
    }

    private RecipeEntity getRecipeEntity(Map<Long, RecipeEntity> recipesById, Long recipeId) {
        RecipeEntity recipeEntity = recipesById.get(recipeId);
        if (recipeEntity == null) {
            throw new ServiceException(ErrorCode.RECIPE_NOT_FOUND.format(recipeId), ErrorCode.RECIPE_NOT_FOUND);
        }
        return recipeEntity;
    }

    @Override
    public MealPlanEntity getFullEntity(MealPlanWeek week) {
        HouseholdEntity household = CurrentUserHelper.getCurrentHousehold();
        LocalDate weekStart = getWeekStart(week);
        return getMealPlanEntityWithMealSlotsAndRecipes(household, weekStart);
    }

    @Override
    public VersionDto getVersion(MealPlanWeek week) {
        HouseholdEntity household = CurrentUserHelper.getCurrentHousehold();
        LocalDate weekStart = getWeekStart(week);

        MealPlanEntity mealPlan = getMealPlanEntity(household, weekStart);
        return new VersionDto(mealPlan.getVersion());
    }

    private LocalDate getWeekStart(MealPlanWeek week) {
        return switch (week) {
            case CURRENT -> MealPlanDateUtils.getCurrentWeekStart();
            case NEXT -> MealPlanDateUtils.getNextWeekStart();
        };
    }

    private MealPlanEntity getMealPlanEntityWithMealSlotsAndRecipes(HouseholdEntity household, LocalDate weekStart) {
        return mealPlanRepository.findWithMealSlotsAndRecipesByHouseholdIdAndWeekStart(household.getId(), weekStart)
                .orElseThrow(() -> new ServiceException(ErrorCode.MEAL_PLAN_NOT_FOUND.format("current"), ErrorCode.MEAL_PLAN_NOT_FOUND));
    }

    private MealPlanEntity getMealPlanEntityWithMealSlots(HouseholdEntity household, LocalDate weekStart) {
        return mealPlanRepository.findWithMealSlotsByHouseholdIdAndWeekStart(household.getId(), weekStart)
                .orElseThrow(() -> new ServiceException(ErrorCode.MEAL_PLAN_NOT_FOUND.format("current"), ErrorCode.MEAL_PLAN_NOT_FOUND));
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

    private MealSlotEntity createMealSlot(MealSlotUpdateRequest slot, MealPlanEntity mealPlan, RecipeEntity recipe) {
        MealSlotEntity entity = new MealSlotEntity();
        entity.setMealPlan(mealPlan);
        entity.setMealType(slot.mealType());
        entity.setDay(slot.day());
        entity.setNote(slot.note());
        entity.setRecipe(recipe);

        return entity;
    }
}
