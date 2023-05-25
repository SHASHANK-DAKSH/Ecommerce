package com.example.MainProject.entities.cart;

import com.example.MainProject.entities.product.ProductVariation;
import com.example.MainProject.entities.users.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Cart {
    @EmbeddedId
    private CartCompositeKey cartCompositeKey;

    @ManyToOne
    @MapsId("customerUserId")
    private Customer customer;

    @ManyToOne
    @MapsId("ProductVariationId")
    private ProductVariation productVariation;

    private int quantity;

    private Boolean isWishListItem;


}
