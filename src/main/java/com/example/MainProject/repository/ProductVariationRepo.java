package com.example.MainProject.repository;

import com.example.MainProject.entities.product.ProductVariation;
import com.example.MainProject.entities.product.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductVariationRepo extends JpaRepository<ProductVariation,Long> {


    Optional<List<ProductVariation>> findByProducts(Products products);
}
