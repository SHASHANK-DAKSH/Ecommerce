package com.example.MainProject.entities.Orders;

import com.example.MainProject.entities.product.ProductVariation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class OrdersProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private int quantity;

    private int price;

    @ManyToOne
    Orders orders;

    @OneToOne()
    ProductVariation productVariation;

}
