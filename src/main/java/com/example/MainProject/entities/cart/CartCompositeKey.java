package com.example.MainProject.entities.cart;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
@Embeddable
public class CartCompositeKey implements Serializable {
    private Long customerUserId;

    private Long ProductVariationId;
}
