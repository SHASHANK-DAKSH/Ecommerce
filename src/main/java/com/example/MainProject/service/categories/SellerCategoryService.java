package com.example.MainProject.service.categories;

import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.repository.CategoryRepository;
import com.example.MainProject.service.categories.Impl.SellerCategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SellerCategoryService  implements SellerCategoryServiceImpl {
    @Autowired
    CategoryRepository categoryRepository;


    @Override
    public List<Category> getSellerCategory() {
        List<Category> categoryList=categoryRepository.findAll();
        List<Category>leafs=new ArrayList<>();
        for(Category category:categoryList){
           if(!categoryRepository.existsByCategory(category)){
               leafs.add(category);
           }
        }
        return leafs;
    }
}
