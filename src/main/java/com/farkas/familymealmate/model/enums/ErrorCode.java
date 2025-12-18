package com.farkas.familymealmate.model.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    FAMILY_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Family member not found with id: %d"),
    RECIPE_NOT_FOUND(HttpStatus.NOT_FOUND, "Recipe not found with id: %d"),
    HOUSEHOLD_NOT_FOUND(HttpStatus.NOT_FOUND, "Household not found with joinId: %s"),
    MEALPLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "Meal plan not found: %s"),
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "Tag not found with id: %s"),
    INGREDIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Ingredient not found with id: %s"),
    MISSING_HOUSEHOLD_DETAILS(HttpStatus.BAD_REQUEST, "Either householdJoinId or householdName must be provided"),
    NO_AUTHORIZATION(HttpStatus.FORBIDDEN, "You have no authorization to access this resource: %d"),
    INVALID_INGREDIENT_MEASUREMENT(HttpStatus.BAD_REQUEST, "Invalid measurements"),
    ;

    private final HttpStatus httpStatus;
    private final String template;

    ErrorCode(HttpStatus httpStatus, String template) {
        this.httpStatus = httpStatus;
        this.template = template;
    }

    public String format(Object... args) {
        return template.formatted(args);
    }

}
