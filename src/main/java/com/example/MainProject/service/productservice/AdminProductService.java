package com.example.MainProject.service.productservice;

import com.example.MainProject.dto.*;
import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.entities.product.Products;
import com.example.MainProject.entities.users.Seller;
import com.example.MainProject.exception.GenericException;
import com.example.MainProject.repository.CategoryRepository;
import com.example.MainProject.repository.ProductsRepo;
import com.example.MainProject.service.productservice.Impl.AdminProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class AdminProductService implements AdminProductServiceImpl {
    @Autowired
    ProductsRepo productsRepo;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public GenerateResponse activateProduct(Long id) {
        Products products=productsRepo.findById(id).orElse(null);
        if(Objects.isNull(products)){
            throw new GenericException("Id is not valid", HttpStatus.NOT_FOUND);
        }
        if(!products.isActive()){
            products.setActive(true);
            productsRepo.save(products);
            return new GenerateResponse("product Activated");
        }
        return new GenerateResponse("product Already Active");
    }

    @Override
    public GenerateResponse DeactivateProduct(Long id) {
        Products products=productsRepo.findById(id).orElse(null);
        if(Objects.isNull(products)){
            throw new GenericException("Id is not valid", HttpStatus.NOT_FOUND);
        }
        if(products.isActive()){
            products.setActive(false);
            productsRepo.save(products);
            return new GenerateResponse("product De-Activated");
        }
        return new GenerateResponse("product Already De-Active");
    }

    @Override
    public AdminProductDto getProductFromId(Long id) {
        Products products=productsRepo.findById(id).orElse(null);
        if(Objects.isNull(products)){
            throw new GenericException("Product Id not correct",HttpStatus.NOT_FOUND);
        }
        Category category=products.getCategory();
        AdminProductDto adminProductDto=new AdminProductDto();
        adminProductDto.setProducts(products);
        adminProductDto.setCategory(category);
        return adminProductDto;
    }

    @Override
    public List<AdminProductResponseDto> getAllProduct(){

        List<Products>productsList=productsRepo.findAll();

        List<AdminProductResponseDto>ans=new ArrayList<>();
        for (Products p : productsList) {

            if (p.isActive() && !p.isDeleted()) {
                AdminProductResponseDto product = new AdminProductResponseDto();
                product.setId(p.getId());
                product.setName(p.getName());
                product.setCategory(p.getCategory());
                product.setBrand(p.getBrand());
                if (p.getProductVariationSet().size() != 0) {
                    product.setProductVariationList(p.getProductVariationSet());
                }
                ans.add(product);
            }

        }
        return ans;
    }
}
