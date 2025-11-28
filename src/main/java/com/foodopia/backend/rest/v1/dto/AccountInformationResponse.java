package com.foodopia.backend.rest.v1.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountInformationResponse {

    private List<UserLight> friends;
    private String email;
    private String firstName;
    private String lastName;

}
