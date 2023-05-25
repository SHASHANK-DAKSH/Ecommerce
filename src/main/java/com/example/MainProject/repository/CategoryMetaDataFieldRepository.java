package com.example.MainProject.repository;

import com.example.MainProject.entities.categories.CategoryMetaDataField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryMetaDataFieldRepository extends JpaRepository<CategoryMetaDataField,Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<CategoryMetaDataField> findByName(String key);
}
