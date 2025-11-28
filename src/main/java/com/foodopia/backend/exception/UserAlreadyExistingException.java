package com.foodopia.backend.exception;

import com.foodopia.backend.rest.v1.errorHandling.ErrorCode;

public class UserAlreadyExistingException extends AuthorizationResponseException {

    public UserAlreadyExistingException(String message, ErrorCode code) {
        super(message, code);
    }
}
