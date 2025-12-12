package com.farkas.familymealmate.model.dto.error;

public record FieldValidationError(
        String field,
        String message,
        Object rejectedValue
) {
}
