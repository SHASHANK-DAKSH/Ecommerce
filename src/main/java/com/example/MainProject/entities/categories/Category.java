package com.example.MainProject.entities.categories;

import com.example.MainProject.entities.audit.Auditable;
import com.example.MainProject.entities.product.Products;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Category extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    //self join

    @ManyToOne// child category associated with only one parent id(example of address)
    @JoinColumn(name="parentCategoryId")
    Category category;


    @JsonIgnore
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,orphanRemoval = true)
    List<Products>products=new ArrayList<>();

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,orphanRemoval = true)
    List<CategoryMetaDataFieldValues>categoryMetaDataFieldValuesList;

}
