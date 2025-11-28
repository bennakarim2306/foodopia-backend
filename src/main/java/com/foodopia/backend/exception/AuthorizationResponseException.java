package com.foodopia.backend.exception;


import com.foodopia.backend.rest.v1.errorHandling.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthorizationResponseException extends Exception {

    private ErrorCode code;

    public AuthorizationResponseException(String message) {
        super(message);
    }

    public AuthorizationResponseException(String message, ErrorCode code) {
        super(message);
        this.code = code;
    }
}
