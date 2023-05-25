package com.example.MainProject.repository;

import com.example.MainProject.entities.product.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Products,Long> {
}
