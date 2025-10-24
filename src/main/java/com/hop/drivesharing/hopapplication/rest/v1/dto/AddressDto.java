// src/main/java/com/hop/drivesharing/hopapplication/rest/v1/dto/AddressDto.java
package com.hop.drivesharing.hopapplication.rest.v1.dto;

import lombok.Data;

@Data
public class AddressDto {
    private String city;
    private String street;
    private String zip;
    private double lat;
    private double lng;
}
