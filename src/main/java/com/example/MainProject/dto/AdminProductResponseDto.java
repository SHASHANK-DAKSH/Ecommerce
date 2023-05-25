package com.example.MainProject.dto;

import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.entities.product.ProductVariation;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AdminProductResponseDto {
    private Long id;
    private String name;
    private String brand;
    private Category category;
    List<ProductVariation> productVariationList;
}
