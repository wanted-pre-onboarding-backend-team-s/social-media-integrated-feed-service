package com.wanted.feed.exception;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(WantedException.class)
    public ResponseEntity<ErrorResponse> handleWantedException(WantedException e) {
        ErrorType errorType = e.getErrorType();
        log.info("[error] type: {}, sts: {}, msg: {}", errorType.getClassType().getSimpleName(), errorType.getHttpStatus(), errorType.getMessage());
        return ResponseEntity.status(e.getErrorType().getHttpStatus()).body(ErrorResponse.of(e.getErrorType()));
    }

}