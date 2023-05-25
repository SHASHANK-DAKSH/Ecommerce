package com.example.MainProject.service.categories;


import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.exception.CategoryException;
import com.example.MainProject.repository.CategoryRepository;
import com.example.MainProject.service.categories.Impl.CustomerCategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CustomerCategoryService  implements CustomerCategoryServiceImpl {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> getCustomerData(Long id) {
       // List<Category>categories=categoryRepository.findAll();
        if(id!=null) {
            Category category = categoryRepository.findById(id).orElse(null);
            if (Objects.isNull(category)) {
                throw new CategoryException("wrong ID", HttpStatus.NOT_FOUND);
            }

            if(!categoryRepository.existsByCategory(category)){
                throw new CategoryException("either pass root Id or nothing",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
           List<Category>parentChild=categoryRepository.findAllByCategory(category);

            return parentChild;
        }
        List<Category>parents=categoryRepository.findAllByCategory(null);
        return parents;
    }
}
