package com.farkas.familymealmate.security.aspect;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.repository.RecipeRepository;
import com.farkas.familymealmate.security.CurrentUserService;
import com.farkas.familymealmate.security.annotation.CheckHouseholdAccess;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class HouseholdSecurityAspect {

    private final CurrentUserService currentUserService;
    private final RecipeRepository recipeRepository;

    @Around("@annotation(checkHouseholdAccess)")
    public Object checkRecipeAccess(ProceedingJoinPoint joinPoint, CheckHouseholdAccess checkHouseholdAccess) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();

        final Long recipeId = extractRecipeId(args, parameterNames, checkHouseholdAccess.idArg());

        RecipeEntity recipeEntity = recipeRepository.findById(recipeId).orElseThrow(
                () -> new ServiceException(ErrorCode.RECIPE_NOT_FOUND.format(recipeId), ErrorCode.RECIPE_NOT_FOUND));

        if (!recipeEntity.getHousehold().getId().equals(currentUserService.getCurrentHousehold().getId())) {
            throw new ServiceException(ErrorCode.NO_AUTHORIZATION.getTemplate(), ErrorCode.NO_AUTHORIZATION);
        }

        return joinPoint.proceed();
    }

    private Long extractRecipeId(Object[] args, String[] paramNames, String idArg) {
        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(idArg) && args[i] instanceof Long) {

                return (Long) args[i];
            }
        }

        throw new IllegalArgumentException("Argument '" + idArg + "' not found");
    }
}
