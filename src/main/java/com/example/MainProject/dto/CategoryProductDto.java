package com.example.MainProject.dto;

import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.entities.product.ProductVariation;
import com.example.MainProject.entities.product.Products;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CategoryProductDto {
    private List<Category>categories;
    private List<Products>productsList;
    private List<ProductVariation>productVariations;
}
