package com.foodopia.backend.exception;

import com.foodopia.backend.rest.v1.errorHandling.ErrorCode;

public class InvalidExceptionUsername extends AuthorizationResponseException {
    public InvalidExceptionUsername(String message, ErrorCode code) {
        super(message, code);
    }
}
