package com.example.MainProject.dto;

import com.example.MainProject.entities.categories.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductResponseDto {
    private Long id;
    private String name;
    private String brand;
    private Category category;
}
