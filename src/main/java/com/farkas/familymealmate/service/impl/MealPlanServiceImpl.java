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
import com.farkas.familymealmate.repository.MealPlanRepository;
import com.farkas.familymealmate.security.CurrentUserService;
import com.farkas.familymealmate.service.MealPlanService;
import com.farkas.familymealmate.service.RecipeService;
import com.farkas.familymealmate.util.MealPlanDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${mealplan.max-favourites:5}")
    private int maxFavourites;

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
    public MealPlanDetailsDto getCurrentWeek() {
        MealPlanEntity mealPlan = getCurrentWeekMealPlanEntity();
        return mealPlanMapper.toDto(mealPlan);

    }

    @Override
    public MealPlanDetailsDto getNextWeek() {
        MealPlanEntity mealPlan = getNextWeekMealPlanEntity();
        return mealPlanMapper.toDto(mealPlan);
    }

    @Override
    public MealPlanDetailsDto editCurrentWeek(MealPlanUpdateRequest mealPlanRequest) {
        MealPlanEntity mealPlanEntity = getCurrentWeekMealPlanEntity();
        return editMealPlan(mealPlanRequest, mealPlanEntity);
    }

    @Override
    public MealPlanDetailsDto editNextWeek(MealPlanUpdateRequest mealPlanRequest) {
        MealPlanEntity mealPlanEntity = getNextWeekMealPlanEntity();
        return editMealPlan(mealPlanRequest, mealPlanEntity);
    }

    @Override
    public void markFavourite(Long id) {
        HouseholdEntity household = currentUserService.getCurrentHousehold();
        checkFavouriteCount(household);
        MealPlanEntity mealPlanEntity = getMealPlanEntity(id);
        checkAlreadyTemplate(mealPlanEntity);
        MealPlanEntity clone = cloneMealPlan(mealPlanEntity, household);

        mealPlanRepository.save(clone);
    }

    @Override
    public void deleteFavourite(Long id) {
        MealPlanEntity mealPlan = getMealPlanEntity(id);
        if (mealPlan.isTemplate()) {
            mealPlan.getMealSlots().clear();
            mealPlanRepository.deleteById(id);
        } else {
            throw new ServiceException(
                    ErrorCode.MEAL_NOT_A_TEMPLATE.format(id), ErrorCode.MEAL_NOT_A_TEMPLATE);
        }
    }

    @Override
    public List<MealPlanDetailsDto> listFavourites() {
        HouseholdEntity household = currentUserService.getCurrentHousehold();
        List<MealPlanEntity> favourites = mealPlanRepository.findAllByHouseholdIdAndTemplate(household.getId(), true);
        return mealPlanMapper.toDtoList(favourites);
    }

    private void checkAlreadyTemplate(MealPlanEntity mealPlanEntity) {
        if (!mealPlanEntity.isTemplate()) {
            throw new ServiceException(
                    ErrorCode.MEAL_PLAN_ALREADY_TEMPLATE.getTemplate(), ErrorCode.MEAL_PLAN_ALREADY_TEMPLATE);
        }
    }

    private void checkFavouriteCount(HouseholdEntity household) {
        long countFavourites = mealPlanRepository.countByHouseholdIdAndTemplate(household.getId(), true);
        if (countFavourites > maxFavourites) {
            throw new ServiceException(
                    ErrorCode.MAXIMUM_MEAL_PLAN_REACHED.getTemplate(), ErrorCode.MAXIMUM_MEAL_PLAN_REACHED);
        }
    }

    private MealPlanEntity getMealPlanEntity(Long id) {
        return mealPlanRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorCode.MEAL_PLAN_NOT_FOUND.format(id.toString()), ErrorCode.MEAL_PLAN_NOT_FOUND));
    }

    private MealPlanEntity cloneMealPlan(MealPlanEntity mealPlanEntity, HouseholdEntity household) {
        MealPlanEntity clone = new MealPlanEntity();
        clone.setTemplate(true);
        clone.setHousehold(household);
        cloneMealSlots(mealPlanEntity, clone);

        return clone;
    }

    private void cloneMealSlots(MealPlanEntity mealPlanEntity, MealPlanEntity clone) {
        List<MealSlotEntity> slots = mealPlanEntity.getMealSlots().stream()
                .map(slot -> {
                    MealSlotEntity cloneSlot = new MealSlotEntity();
                    cloneSlot.setMealType(slot.getMealType());
                    cloneSlot.setMealPlan(clone);
                    cloneSlot.setNote(slot.getNote());
                    cloneSlot.setRecipe(slot.getRecipe());
                    cloneSlot.setDay(slot.getDay());

                    return cloneSlot;
                }).toList();

        clone.setMealSlots(slots);
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

    private MealPlanEntity getCurrentWeekMealPlanEntity() {
        HouseholdEntity currentHousehold = currentUserService.getCurrentHousehold();
        LocalDate weekStart = MealPlanDateUtils.getCurrentWeekStart();
        return getMealPlanEntity(currentHousehold, weekStart);
    }

    private MealPlanEntity getNextWeekMealPlanEntity() {
        HouseholdEntity currentHousehold = currentUserService.getCurrentHousehold();
        LocalDate weekStart = MealPlanDateUtils.getNextWeekStart();
        return getMealPlanEntity(currentHousehold, weekStart);
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
                    entity.setRecipe(recipeService.getEntity(slot.recipeId()));
                    entity.setNote(slot.note());
                    entity.setDay(slot.day());
                    entity.setMealType(slot.mealType());
                });

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
