package com.apps.quantitymeasurement.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e, HttpServletRequest req) {
        var fieldErrors = e.getBindingResult().getFieldErrors();
        String msg;
        if (fieldErrors == null || fieldErrors.isEmpty()) {
            msg = e.getMessage();
        } else {
            msg = fieldErrors.stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse(e.getMessage());
        }
        return build(HttpStatus.BAD_REQUEST, "Validation Error", msg, req);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException e, HttpServletRequest req) {
        String msg = e.getMostSpecificCause() != null ? e.getMostSpecificCause().getMessage() : e.getMessage();
        return build(HttpStatus.BAD_REQUEST, "Invalid Request Body", msg, req);
    }

    @ExceptionHandler({CategoryMismatchException.class, QuantityMeasurementException.class, InvalidUnitException.class})
    public ResponseEntity<ErrorResponse> handleBusinessLogic(RuntimeException e, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Quantity Measurement Error", e.getMessage(), req);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException e, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Unauthorized", e.getMessage(), req);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String err, String msg, HttpServletRequest req) {
        ErrorResponse res = new ErrorResponse(LocalDateTime.now(), status.value(), err, msg, req.getRequestURI());
        return new ResponseEntity<>(res, status);
    }
}