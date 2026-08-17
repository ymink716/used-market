package com.ymink716.used_market.common;

import com.ymink716.used_market.common.exception.*;
import com.ymink716.used_market.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
        UserNotFoundException e,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        return ResponseEntity
            .status(status)
            .body(new ErrorResponse(
                status,
                e.getMessage(),
                request.getRequestURI()
            ));
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUser(
        DuplicateUserException e,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.CONFLICT;

        return ResponseEntity
            .status(status)
            .body(new ErrorResponse(
                status,
                e.getMessage(),
                request.getRequestURI()
            ));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPassword(
        InvalidPasswordException e,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        return ResponseEntity
            .status(status)
            .body(new ErrorResponse(
                status,
                e.getMessage(),
                request.getRequestURI()
            ));
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleItemNotFound(
        ItemNotFoundException e,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        return ResponseEntity
            .status(status)
            .body(new ErrorResponse(
                status,
                e.getMessage(),
                request.getRequestURI()
            ));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(
        ForbiddenException e,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        return ResponseEntity
            .status(status)
            .body(new ErrorResponse(
                status,
                e.getMessage(),
                request.getRequestURI()
            ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
        MethodArgumentNotValidException e,
        HttpServletRequest request
    ) {
        String message = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(FieldError::getDefaultMessage)
            .orElse("잘못된 요청입니다.");

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity
            .status(status)
            .body(new ErrorResponse(
                status,
                message,
                request.getRequestURI()
            ));
    }
}