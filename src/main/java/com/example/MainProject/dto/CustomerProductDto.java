package com.example.MainProject.dto;

import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.entities.product.ProductVariation;
import com.example.MainProject.entities.product.Products;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerProductDto {
    private Products products;
    private Category category;
    private List<ProductVariation> productVariation;
}
