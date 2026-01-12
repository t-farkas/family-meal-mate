package com.farkas.familymealmate.model.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    FAMILY_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Family member not found with id: %d"),

    RECIPE_NOT_FOUND(HttpStatus.NOT_FOUND, "Recipe not found with id: %d"),
    INGREDIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Ingredient not found with id: %s"),
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "Tag not found with id: %s"),

    HOUSEHOLD_NOT_FOUND(HttpStatus.NOT_FOUND, "Household not found with joinId: %s"),
    MISSING_HOUSEHOLD_DETAILS(HttpStatus.BAD_REQUEST, "Either householdJoinId or householdName must be provided"),

    MEAL_PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "Meal plan not found: %s"),
    MEAL_SLOT_NOT_FOUND(HttpStatus.NOT_FOUND, "Meal slot not found: %s"),
    MAXIMUM_MEAL_PLAN_REACHED(HttpStatus.CONFLICT, "Maximum number of templates reached"),
    MEAL_PLAN_VERSION_MISMATCH(HttpStatus.CONFLICT, "Meal plan was modified by another user"),

    TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "Template not found with id: %s"),
    TEMPLATE_NAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Template with name %s already exists"),

    SHOPPING_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "No shopping list found"),
    SHOPPING_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "Shopping item not found with id: %s"),
    SHOPPING_LIST_VERSION_MISMATCH(HttpStatus.CONFLICT, "Shopping list was modified by another user"),

    SHOPPING_ITEM_INCOMPLETE_DETAILS(HttpStatus.BAD_REQUEST, "Shopping item must have either name or ingredientId"),

    INCOMPATIBLE_UNITS(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot convert %s to %s"),
    NO_AUTHORIZATION(HttpStatus.FORBIDDEN, "You have no authorization to access this resource: %d"),
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
