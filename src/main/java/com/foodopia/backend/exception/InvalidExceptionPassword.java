package com.foodopia.backend.exception;

import com.foodopia.backend.rest.v1.errorHandling.ErrorCode;

public class InvalidExceptionPassword extends AuthorizationResponseException {
    public InvalidExceptionPassword(String message, ErrorCode code) {
        super(message, code);
    }
}
