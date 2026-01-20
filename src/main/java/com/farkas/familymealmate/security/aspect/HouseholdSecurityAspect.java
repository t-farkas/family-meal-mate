package com.farkas.familymealmate.security.aspect;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.model.common.HouseholdOwned;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.model.enums.HouseholdOwnedResourceType;
import com.farkas.familymealmate.repository.FamilyMemberRepository;
import com.farkas.familymealmate.repository.MealPlanRepository;
import com.farkas.familymealmate.repository.RecipeRepository;
import com.farkas.familymealmate.security.CurrentUserHelper;
import com.farkas.familymealmate.security.annotation.CheckHouseholdAccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class HouseholdSecurityAspect {

    private final RecipeRepository recipeRepository;
    private final MealPlanRepository mealPlanRepository;
    private final FamilyMemberRepository familyMemberRepository;

    @Around("@annotation(checkHouseholdAccess)")
    public Object checkHouseholdAccess(ProceedingJoinPoint joinPoint, CheckHouseholdAccess checkHouseholdAccess) throws Throwable {
        final Long householdOwnedId = extractId(joinPoint, checkHouseholdAccess.idArg());
        Long householdId = CurrentUserHelper.getCurrentHousehold().getId();

        HouseholdOwnedResourceType resourceType = checkHouseholdAccess.type();
        HouseholdOwned householdOwnedEntity = switch (resourceType) {
            case RECIPE -> getRecipe(householdOwnedId);
            case MEAL_PLAN -> getMealPlan(householdOwnedId);
            case FAMILY_MEMBER -> getFamilyMember(householdOwnedId);
        };

        if (!householdOwnedEntity.getHousehold().getId().equals(householdId)) {
            throw new ServiceException(ErrorCode.NO_AUTHORIZATION.format(householdOwnedId), ErrorCode.NO_AUTHORIZATION);
        }

        log.info("Household access granted for resource type {}, with id {}", resourceType.name(), householdOwnedId);

        return joinPoint.proceed();
    }

    private Long extractId(ProceedingJoinPoint joinPoint, String idArg) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();

        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(idArg) && args[i] instanceof Long) {

                return (Long) args[i];
            }
        }

        throw new IllegalArgumentException("Argument '" + idArg + "' not found");
    }

    private HouseholdOwned getMealPlan(Long id) {
        return mealPlanRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorCode.MEAL_PLAN_NOT_FOUND.format(id), ErrorCode.MEAL_PLAN_NOT_FOUND));
    }

    private HouseholdOwned getRecipe(Long id) {
        return recipeRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorCode.RECIPE_NOT_FOUND.format(id), ErrorCode.RECIPE_NOT_FOUND));
    }

    private HouseholdOwned getFamilyMember(Long id) {
        return familyMemberRepository.findById(id).orElseThrow(
                () -> new ServiceException(ErrorCode.FAMILY_MEMBER_NOT_FOUND.format(id), ErrorCode.FAMILY_MEMBER_NOT_FOUND));

    }

}
