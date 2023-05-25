package com.example.MainProject.dto;

import com.example.MainProject.entities.categories.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryResponseViewDto {
    private List<?> parent;
    private Category node;
    private List<Category> child;
}
