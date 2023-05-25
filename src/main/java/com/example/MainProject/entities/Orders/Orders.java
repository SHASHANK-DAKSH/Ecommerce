package com.example.MainProject.entities.Orders;

import com.example.MainProject.entities.users.Address;
import com.example.MainProject.entities.users.Customer;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Data
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private int amountPaid;

    private Date dateCreated;

    private String paymentMethod;

    @ManyToOne
    private Customer customer;


    // address of all customer will be fetched from above customer mapping
//
//    @OneToMany
//    @JoinColumn(name="customerAddress")
//    private List<Address> addresses;

    @OneToMany(mappedBy = "orders",cascade = CascadeType.ALL,orphanRemoval = true)
    List<OrdersProduct> ordersProduct=new ArrayList<>();



}
