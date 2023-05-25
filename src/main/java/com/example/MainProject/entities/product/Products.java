package com.example.MainProject.entities.product;

import com.example.MainProject.entities.audit.Auditable;
import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.entities.users.Seller;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Products extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private String name;

    private String description;

    private boolean isCancellable;

    private boolean isReturnAble;

    private String Brand;

    private boolean isActive;

    private boolean isDeleted;

    @JsonIgnore
    @ManyToOne
    private Seller seller;

    @JsonIgnore
    @ManyToOne
    private Category category;


    @JsonIgnore
    @OneToMany(mappedBy = "products",cascade = CascadeType.ALL,orphanRemoval = true)
    List<ProductVariation> productVariationSet;

    @OneToMany(mappedBy = "products",cascade = CascadeType.ALL,orphanRemoval = true)
    List<ProductReview> productReviewList;

}
