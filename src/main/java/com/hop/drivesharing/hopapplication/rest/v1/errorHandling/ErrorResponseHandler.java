package com.hop.drivesharing.hopapplication.rest.v1.errorHandling;


import com.hop.drivesharing.hopapplication.exception.*;
import com.hop.drivesharing.hopapplication.rest.v1.dto.AuthenticationErrorResponse;
import com.hop.drivesharing.hopapplication.rest.v1.dto.RestErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ErrorResponseHandler {

    @ExceptionHandler(value = {AuthorizationResponseException.class, UserAlreadyExistingException.class, InvalidExceptionPassword.class, InvalidExceptionUsername.class})
    protected ResponseEntity<AuthenticationErrorResponse> handleException(AuthorizationResponseException e, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(new AuthenticationErrorResponse(e.getMessage(), e.getCode()));
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    protected ResponseEntity<AuthenticationErrorResponse> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthenticationErrorResponse(e.getMessage(), ErrorCode.AUTHENTICATION_FAILED));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<AuthenticationErrorResponse> handleAuthenticationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessage;
        if (e.getMessage().contains("Blank") && e.getMessage().contains("email")) {
            errorMessage = "Please provide an email address";
        } else if (e.getMessage().contains("Pattern") && e.getMessage().contains("email")) {
            errorMessage = "Please provide a valid email address";
        } else if (e.getMessage().contains("Blank") && e.getMessage().contains("password")) {
            errorMessage = "Please provide a password";
        } else if (e.getMessage().contains("Pattern") && e.getMessage().contains("password")) {
            errorMessage = "Please provide a valid password address";
        } else {
            errorMessage = "Some validation error occurred";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthenticationErrorResponse(errorMessage, ErrorCode.INPUT_VALIDATION_ERROR));
    }

    @ExceptionHandler(value = {AddContactToListException.class})
    protected ResponseEntity<RestErrorResponse> handleAddContactException(Exception e, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(RestErrorResponse.builder().message(e.getMessage()).build());
    }
}
