package com.hop.drivesharing.hopapplication.rest.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * this data represents for example the Contact object in the response delivered when calling get Contacts list
 * or when being notified for a Contact add request vie webSocket
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class UserLight {

    private String id;
    private String email;
    private String firstName;
    private String lastName;

    //TODO here come more information about the user data retrieved for example profile picture

}
