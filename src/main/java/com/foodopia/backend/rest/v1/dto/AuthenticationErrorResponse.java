package com.foodopia.backend.rest.v1.dto;


import com.foodopia.backend.rest.v1.errorHandling.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class AuthenticationErrorResponse {

    @NonNull
    String message;
    @NonNull
    ErrorCode errorCode;


}
