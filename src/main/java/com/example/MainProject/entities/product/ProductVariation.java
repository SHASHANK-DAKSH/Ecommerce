package com.example.MainProject.entities.product;

import com.example.MainProject.Converter.HashMapConverter;
import com.example.MainProject.entities.Orders.OrdersProduct;
import com.example.MainProject.entities.audit.Auditable;
import com.example.MainProject.entities.cart.Cart;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity

public class ProductVariation extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Products products;

    private int quantityAvailable;

    private long price;

    @Convert(converter = HashMapConverter.class)
    private Map<String, String> customerAttributes;//refactor name later.

//    @Lob
//    @Column(length = 1000)
//    private byte[]primaryImage; // added string path.

    private Boolean isActive;

    @JsonIgnore//not used
    @OneToOne(mappedBy = "productVariation",cascade = CascadeType.ALL,orphanRemoval = true)
    OrdersProduct ordersProduct;


    @JsonIgnore//not used
    @OneToMany(mappedBy = "productVariation",cascade = CascadeType.ALL,orphanRemoval = true)
    List<Cart>cartList=new ArrayList<>();

    private String image;

}