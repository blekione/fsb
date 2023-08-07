package com.blekione.fsb.exception;

import com.blekione.fsb.model.dto.ApiErrorDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<ApiErrorDto> handleApplicationException(ApplicationException ex) {
        ApiErrorDto apiError = ApiErrorDto.create(HttpStatus.CONFLICT, ex);
        return ResponseEntity.badRequest().body(apiError);
    }
}
