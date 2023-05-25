package com.example.MainProject.service.productservice.Impl;

import com.example.MainProject.dto.CategoryProductDto;
import com.example.MainProject.dto.CustomerProductDto;
import com.example.MainProject.entities.product.Products;

import java.util.List;

public interface CustomerProductServiceImpl {

    public CustomerProductDto getProductDetails(Long id);

    public CategoryProductDto getAllProductFromCategory(Long id);

    public List<Products> SimilarProduct(Long id);
}
