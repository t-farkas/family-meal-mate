package com.farkas.familymealmate.model.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    FAMILY_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Family member not found with id: %d"),
    HOUSEHOLD_NOT_FOUND(HttpStatus.NOT_FOUND, "Household not found with joinId: %s"),
    MISSING_HOUSEHOLD_DETAILS(HttpStatus.BAD_REQUEST, "Household not found with joinId: %s"),
    NO_AUTHORIZATION(HttpStatus.FORBIDDEN, "Either householdJoinId or householdName must be provided"),
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
