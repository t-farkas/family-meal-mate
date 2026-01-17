package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.mapper.MealPlanMapper;
import com.farkas.familymealmate.model.dto.template.TemplateCreateRequest;
import com.farkas.familymealmate.model.dto.template.TemplateDto;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.MealPlanEntity;
import com.farkas.familymealmate.model.entity.MealSlotEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.model.enums.HouseholdOwnedResourceType;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.repository.MealPlanRepository;
import com.farkas.familymealmate.security.CurrentUserHelper;
import com.farkas.familymealmate.security.annotation.CheckHouseholdAccess;
import com.farkas.familymealmate.service.TemplateService;
import com.farkas.familymealmate.util.MealPlanDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TemplateServiceImpl implements TemplateService {

    private final MealPlanRepository mealPlanRepository;
    private final MealPlanMapper mealPlanMapper;

    @Value("${mealplan.max-favourites:5}")
    private int maxFavourites;

    @Override
    public TemplateDto createTemplate(TemplateCreateRequest request) {
        HouseholdEntity household = CurrentUserHelper.getCurrentHousehold();
        checkTemplateCount(household);
        MealPlanEntity template = createTemplate(household, request);

        MealPlanEntity saved = save(template);
        return mealPlanMapper.toTemplateDto(saved);
    }

    @Override
    @CheckHouseholdAccess(type = HouseholdOwnedResourceType.MEAL_PLAN)
    public void deleteTemplate(Long id) {
        MealPlanEntity mealPlan = getMealPlanEntity(id);
        if (mealPlan.isTemplate()) {
            mealPlanRepository.deleteById(id);
        } else {
            throw new ServiceException(
                    ErrorCode.TEMPLATE_NOT_FOUND.format(id), ErrorCode.TEMPLATE_NOT_FOUND);
        }
    }

    @Override
    public List<TemplateDto> listTemplates() {
        HouseholdEntity household = CurrentUserHelper.getCurrentHousehold();
        List<MealPlanEntity> favourites = mealPlanRepository.findAllByHouseholdIdAndTemplate(household.getId(), true);
        return mealPlanMapper.toTemplateDtoList(favourites);
    }

    private LocalDate getWeekStart(MealPlanWeek week) {
        return switch (week) {
            case CURRENT -> MealPlanDateUtils.getCurrentWeekStart();
            case NEXT -> MealPlanDateUtils.getNextWeekStart();
        };
    }

    private void checkTemplateCount(HouseholdEntity household) {
        long countFavourites = mealPlanRepository.countByHouseholdIdAndTemplate(household.getId(), true);
        if (countFavourites > maxFavourites) {
            throw new ServiceException(ErrorCode.MAXIMUM_MEAL_PLAN_REACHED);
        }
    }

    private MealPlanEntity getMealPlanEntity(Long id) {
        return mealPlanRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorCode.MEAL_PLAN_NOT_FOUND.format(id.toString()), ErrorCode.MEAL_PLAN_NOT_FOUND));
    }

    private MealPlanEntity createTemplate(HouseholdEntity household, TemplateCreateRequest request) {
        LocalDate weekStart = getWeekStart(request.week());
        MealPlanEntity mealPlanEntity = getMealPlanEntity(household, weekStart);
        MealPlanEntity template = cloneMealPlan(mealPlanEntity, household);
        template.setTemplateName(request.name());
        return template;
    }

    private MealPlanEntity cloneMealPlan(MealPlanEntity mealPlan, HouseholdEntity household) {
        MealPlanEntity clone = new MealPlanEntity();
        clone.setTemplate(true);
        clone.setHousehold(household);
        cloneMealSlots(mealPlan, clone);

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

    private MealPlanEntity getMealPlanEntity(HouseholdEntity currentHousehold, LocalDate weekStart) {
        return mealPlanRepository.findByHouseholdIdAndWeekStart(currentHousehold.getId(), weekStart)
                .orElseThrow(() -> new ServiceException(ErrorCode.MEAL_PLAN_NOT_FOUND.format("current"), ErrorCode.MEAL_PLAN_NOT_FOUND));
    }

    private MealPlanEntity save(MealPlanEntity template) {
        try {
            return mealPlanRepository.save(template);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException(
                    ErrorCode.TEMPLATE_NAME_ALREADY_EXISTS.format(template.getTemplateName()),
                    ErrorCode.TEMPLATE_NAME_ALREADY_EXISTS);
        }
    }

}
