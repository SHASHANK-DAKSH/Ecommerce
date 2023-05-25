package com.example.MainProject.entities.product;

import com.example.MainProject.entities.users.Customer;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ProductReview {
    @EmbeddedId
    private ProductReviewComposite productReviewComposite;

    @ManyToOne
    @MapsId("customerId")
    private Customer customer;

    @ManyToOne
    @MapsId("productId")
    private Products products;

    private String review;

    private float rating;
}
