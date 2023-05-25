package com.example.MainProject.repository;

import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.entities.product.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.method.P;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductsRepo extends JpaRepository<Products,Long> {
    Optional<Products> findByName(String name);

    boolean existsByName(String name);

    Optional<List<Products>>findAllByCategory(Category c);

    List<Products> findAllByName(String name);
}
