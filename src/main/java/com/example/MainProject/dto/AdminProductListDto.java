package com.example.MainProject.dto;

import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.entities.product.Products;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AdminProductListDto {
    List<Products>productsList;
    List<Category>categoryList;
}
