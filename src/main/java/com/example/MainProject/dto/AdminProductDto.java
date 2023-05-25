package com.example.MainProject.dto;

import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.entities.product.Products;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdminProductDto {
    private Products products;
    private Category category;
}
