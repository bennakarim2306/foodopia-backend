package com.hop.drivesharing.hopapplication.data.item;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Quantity {

    private int quantityAmount;
    private String quantityUnit;

    // Getters and Setters
}