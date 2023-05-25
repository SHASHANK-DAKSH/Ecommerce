package com.example.MainProject.repository;

import com.example.MainProject.entities.categories.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByNameIgnoreCase(String parentName);

    Optional<Category> findByNameIgnoreCase(String parentName);

    List<Category> findAllByCategory(Category node);

    boolean existsByCategory(Category category);
}
