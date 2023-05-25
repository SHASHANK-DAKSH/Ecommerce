package com.example.MainProject.entities.users;

import com.example.MainProject.entities.product.Products;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name="userId")
public class Seller extends User {
    private String gstNumber;
    private String CompanyContact;
    private String companyName;

    @OneToOne(mappedBy = "seller",cascade = CascadeType.ALL,orphanRemoval = true)
    Address address;

    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL,orphanRemoval = true)
    Set<Products>products=new HashSet<>();
}
