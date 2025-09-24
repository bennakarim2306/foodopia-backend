package com.hop.drivesharing.hopapplication.data.item;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "items")
@Table(name = "items", schema = "business_data")
public class Item {

    @Id
    private String id;

    private String name;
    private String type;
    private double price;
    private double quantity;
    private String unit;

    @Embedded
    private Address address;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "seller_id")),
        @AttributeOverride(name = "name", column = @Column(name = "seller_name")),
        @AttributeOverride(name = "contact", column = @Column(name = "seller_contact"))
    })
    private Seller seller;

    private java.time.LocalDate availableFrom;
    private java.time.LocalDate availableTo;

    private String description;
    private String imageUrl;

    // Getters and Setters
}