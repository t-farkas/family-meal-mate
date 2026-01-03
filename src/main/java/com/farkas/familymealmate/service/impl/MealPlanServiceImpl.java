package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.mapper.MealPlanMapper;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.dto.mealplan.MealSlotUpdateRequest;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.MealPlanEntity;
import com.farkas.familymealmate.model.entity.MealSlotEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.repository.MealPlanRepository;
import com.farkas.familymealmate.security.CurrentUserService;
import com.farkas.familymealmate.service.MealPlanService;
import com.farkas.familymealmate.service.RecipeService;
import com.farkas.familymealmate.util.MealPlanDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MealPlanServiceImpl implements MealPlanService {

    private final CurrentUserService currentUserService;
    private final MealPlanRepository mealPlanRepository;
    private final MealPlanMapper mealPlanMapper;
    private final RecipeService recipeService;

    @Override
    public void createMealPlans() {
        HouseholdEntity household = currentUserService.getCurrentHousehold();

        LocalDate currentWeekStart = MealPlanDateUtils.getCurrentWeekStart();
        Optional<MealPlanEntity> currentWeekMealPlan = getMealPlanOptional(household, currentWeekStart);
        if (currentWeekMealPlan.isEmpty()) {
            saveMealPlan(currentWeekStart, household);
        }

        LocalDate nextWeekStart = MealPlanDateUtils.getNextWeekStart();
        Optional<MealPlanEntity> nextWeekMealPlan = getMealPlanOptional(household, nextWeekStart);
        if (nextWeekMealPlan.isEmpty()) {
            saveMealPlan(nextWeekStart, household);
        }

    }

    @Override
    public MealPlanDetailsDto getMealPlan(MealPlanWeek week) {
        HouseholdEntity household = currentUserService.getCurrentHousehold();
        LocalDate weekStart = getWeekStart(week);
        MealPlanEntity mealPlan = getMealPlanEntity(household, weekStart);

        return mealPlanMapper.toDto(mealPlan);
    }

    @Override
    public MealPlanDetailsDto editMealPlan(MealPlanUpdateRequest mealPlanRequest) {
        HouseholdEntity household = currentUserService.getCurrentHousehold();
        LocalDate weekStart = getWeekStart(mealPlanRequest.week());
        MealPlanEntity mealPlanEntity = getMealPlanEntity(household, weekStart);

        return editMealPlan(mealPlanRequest, mealPlanEntity);
    }

    @Override
    public MealPlanEntity getMealPlanEntity(MealPlanWeek week) {
        HouseholdEntity household = currentUserService.getCurrentHousehold();
        LocalDate weekStart = getWeekStart(week);
        return getMealPlanEntity(household, weekStart);
    }

    private LocalDate getWeekStart(MealPlanWeek week) {
        return switch (week) {
            case CURRENT -> MealPlanDateUtils.getCurrentWeekStart();
            case NEXT -> MealPlanDateUtils.getNextWeekStart();
        };
    }

    private MealPlanDetailsDto editMealPlan(MealPlanUpdateRequest mealPlanRequest, MealPlanEntity mealPlanEntity) {
        List<MealSlotEntity> mealSlotEntities = mealPlanEntity.getMealSlots();
        List<MealSlotUpdateRequest> mealSlots = mealPlanRequest.mealSlots();

        deleteRemoved(mealSlotEntities, mealSlots);
        updateExisting(mealSlotEntities, mealSlots);
        mealSlotEntities.addAll(createNew(mealPlanEntity, mealSlots));

        MealPlanEntity saved = mealPlanRepository.save(mealPlanEntity);
        return mealPlanMapper.toDto(saved);
    }

    private MealPlanEntity getMealPlanEntity(HouseholdEntity currentHousehold, LocalDate weekStart) {
        return mealPlanRepository.findByHouseholdIdAndWeekStart(currentHousehold.getId(), weekStart)
                .orElseThrow(() -> new ServiceException(ErrorCode.MEAL_PLAN_NOT_FOUND.format("current"), ErrorCode.MEAL_PLAN_NOT_FOUND));
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

    private void deleteRemoved(List<MealSlotEntity> mealSlotEntities, List<MealSlotUpdateRequest> mealSlots) {
        List<Long> requestIds = mealSlots.stream().map(MealSlotUpdateRequest::id).toList();
        mealSlotEntities.removeIf(entity -> !requestIds.contains(entity.getId()));
    }

    private void updateExisting(List<MealSlotEntity> mealSlotEntities, List<MealSlotUpdateRequest> mealSlots) {
        Map<Long, MealSlotEntity> entityMap = mealSlotEntities.stream()
                .collect(Collectors.toMap(MealSlotEntity::getId, entity -> entity));

        mealSlots.stream()
                .filter(slot -> slot.id() != null)
                .forEach(slot -> {
                    MealSlotEntity entity = entityMap.get(slot.id());
                    nullCheckEntity(slot, entity);

                    entity.setRecipe(recipeService.getEntity(slot.recipeId()));
                    entity.setNote(slot.note());
                    entity.setDay(slot.day());
                    entity.setMealType(slot.mealType());
                });

    }

    private void nullCheckEntity(MealSlotUpdateRequest slot, MealSlotEntity entity) {
        if (entity == null) {
            throw new ServiceException(
                    ErrorCode.MEAL_SLOT_NOT_FOUND.format(slot.id()),
                    ErrorCode.MEAL_SLOT_NOT_FOUND);
        }
    }

    private List<MealSlotEntity> createNew(MealPlanEntity mealPlan, List<MealSlotUpdateRequest> mealSlots) {
        return mealSlots.stream()
                .filter(slot -> slot.id() == null)
                .map(slot -> {
                    MealSlotEntity entity = new MealSlotEntity();
                    entity.setMealPlan(mealPlan);
                    entity.setMealType(slot.mealType());
                    entity.setDay(slot.day());
                    entity.setNote(slot.note());
                    entity.setRecipe(recipeService.getEntity(slot.recipeId()));

                    return entity;
                }).collect(Collectors.toList());
    }
}
