package com.example.MainProject.service.categories.Impl;

import com.example.MainProject.dto.*;
import com.example.MainProject.entities.categories.Category;

import java.util.List;

public interface AdminCategoryServiceImpl {

    public GenerateResponse addMetaData(CategoryMetadataFieldDto categoryMetadataFieldDto);

    public List<CategoryMetadataFieldDto> showMetaDataField();

    public GenerateResponse addCategory(CategoryDto categoryDto);

    public CategoryResponseViewDto getCategoryData(Long id);

    public List<Category>getAllCategoryData();

    public GenerateResponse updateCategory(Long id, CategoryMetadataFieldDto categoryMetadataFieldDto);

    public GenerateResponse addMetaDataFieldValues(CategoryMetaFieldValueDto categoryMetaFieldValueDto);

    public GenerateResponse updateMetaData(CategoryMetaFieldValueDto categoryMetaFieldValueDto);
}
