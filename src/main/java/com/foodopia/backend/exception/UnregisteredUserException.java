package com.foodopia.backend.exception;

import com.foodopia.backend.rest.v1.errorHandling.ErrorCode;

public class UnregisteredUserException extends AuthorizationResponseException {

    public UnregisteredUserException(String message, ErrorCode code) {
        super(message, code);
    }
}
