package com.example.MainProject.repository;

import com.example.MainProject.entities.categories.CategoryMetaDataFieldValues;
import com.example.MainProject.entities.categories.CategoryMetaDataLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryMetaDataFieldValuesRepository extends
        JpaRepository<CategoryMetaDataFieldValues,Long> {


    Optional<CategoryMetaDataFieldValues> findByCategoryMetaDataLink(CategoryMetaDataLink categoryMetaDataLink);

    boolean existsByCategoryIdAndCategoryMetaDataFieldId(Long id, Long id1);

    Optional<CategoryMetaDataFieldValues> findByCategoryIdAndCategoryMetaDataFieldId(Long id,Long id1);
}
