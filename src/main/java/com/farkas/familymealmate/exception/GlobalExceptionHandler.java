package com.farkas.familymealmate.exception;

import com.farkas.familymealmate.model.dto.error.ErrorDTO;
import com.farkas.familymealmate.model.dto.error.FieldValidationError;
import com.farkas.familymealmate.model.dto.error.ValidationErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorDTO> handleServiceException(ServiceException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage(), ex.getErrorCode().name());
        return new ResponseEntity<>(errorDTO, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldValidationError(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue())).toList();

        ValidationErrorResponse response = new ValidationErrorResponse("Invalid request", errors);

        return ResponseEntity.badRequest().body(response);
    }
}
