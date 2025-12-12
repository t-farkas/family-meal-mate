package com.farkas.familymealmate.model.dto.error;

import java.util.List;

public record ValidationErrorResponse(
        String message,
        List<FieldValidationError> errors
) {
}
