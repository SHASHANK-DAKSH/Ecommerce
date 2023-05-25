package com.example.MainProject.entities.product;

import com.example.MainProject.entities.users.Customer;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class ProductReviewComposite implements Serializable {


    private Long customerId;

    private Long productId;

}
