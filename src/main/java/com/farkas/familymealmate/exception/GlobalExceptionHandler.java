package com.farkas.familymealmate.exception;

import com.farkas.familymealmate.model.dto.error.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorDTO> handleServiceException(ServiceException ex) {
        ErrorDTO errorDTO = new ErrorDTO(ex.getMessage(), ex.getErrorCode());
        return new ResponseEntity<>(errorDTO, ex.getHttpStatus());
    }
}
