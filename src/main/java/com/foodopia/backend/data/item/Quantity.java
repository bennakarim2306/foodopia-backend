package com.foodopia.backend.data.item;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Quantity {

    private int quantityAmount;
    private String quantityUnit;

    // Getters and Setters
}