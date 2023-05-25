package com.example.MainProject.entities.users;

import com.example.MainProject.entities.audit.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Address extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String city;

    private String state;

    private String country;

    private String addressLine;

    private  int zipCode;

    private String Label;

    @OneToOne()
    @JsonIgnore // to remove circular dependency during get method
    Seller seller;

    @ManyToOne()
    @JsonIgnore
    Customer customer;


}
