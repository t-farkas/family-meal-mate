package com.farkas.familymealmate.model.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    FAMILY_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Family member not found with id: %d"),;

    private final HttpStatus httpStatus;
    private final String template;

    ErrorCode(HttpStatus httpStatus, String template) {
        this.httpStatus = httpStatus;
        this.template = template;
    }

    public String format (Object... args){
        return template.formatted(args);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
