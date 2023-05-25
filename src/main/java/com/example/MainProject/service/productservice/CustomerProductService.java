package com.example.MainProject.service.productservice;

import com.example.MainProject.dto.CategoryProductDto;
import com.example.MainProject.dto.CustomerProductDto;
import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.entities.product.ProductVariation;
import com.example.MainProject.entities.product.Products;
import com.example.MainProject.exception.CategoryException;
import com.example.MainProject.exception.GenericException;
import com.example.MainProject.exception.ProductException;
import com.example.MainProject.repository.*;
import com.example.MainProject.service.productservice.Impl.CustomerProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CustomerProductService implements CustomerProductServiceImpl {

    @Autowired
    ProductsRepo productsRepo;
    @Autowired
    CategoryMetaDataFieldRepository categoryMetaDataFieldRepository;
    @Autowired
    CategoryMetaDataFieldValuesRepository categoryMetaDataFieldValuesRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductVariationRepo productVariationRepo;


    @Override
    public CustomerProductDto getProductDetails(Long id){
        Products products=productsRepo.findById(id).orElse(null);
        if(Objects.isNull(products)){
            throw new GenericException("Product is not found", HttpStatus.NOT_FOUND);
        }
        if(products.isDeleted()||!products.isActive()){
            throw new GenericException("product is deleted or InActive",HttpStatus.UNAUTHORIZED);
        }
        List<ProductVariation> productVariation=productVariationRepo.findByProducts(products).orElse(null);
        if(productVariation.size()==0){
            throw new ProductException("this product has no variation",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Category category=categoryRepository.findById(products.getCategory().getId()).get();
        CustomerProductDto customerProductDto=new CustomerProductDto();
        customerProductDto.setProducts(products);
        customerProductDto.setCategory(category);
        customerProductDto.setProductVariation(productVariation);

        return customerProductDto;


    }

    @Override
    public CategoryProductDto getAllProductFromCategory(Long id){
        Category category=categoryRepository.findById(id).orElse(null);
        if(Objects.isNull(category)){
            throw new CategoryException("Category not found",HttpStatus.NOT_FOUND);
        }
        CategoryProductDto categoryProductDto=new CategoryProductDto();
        List<Category>categories=new ArrayList<>();
        if(category.getCategory()==null){//root category
            categories=categoryRepository.findAllByCategory(category);
        } else {// find root then all child
            categories.add(category);
        }

        List<Products>productsList=new ArrayList<>();
        for(Category c:categories){
            List<Products>productsList1=productsRepo.findAllByCategory(c).get();
            addListToList(productsList,productsList1);
        }
        List<ProductVariation>productVariations=new ArrayList<>();
        for(Products p:productsList){
            List<ProductVariation>productVariations1=productVariationRepo.findByProducts(p).get();
            addListToList2(productVariations,productVariations1);
        }
        categoryProductDto.setCategories(categories);
        categoryProductDto.setProductVariations(productVariations);
        categoryProductDto.setProductsList(productsList);

        return categoryProductDto;

    }

    @Override
    public List<Products>SimilarProduct(Long id){
        Products products=productsRepo.findById(id).orElse(null);
        if(Objects.isNull(products)){
            throw new ProductException("Product Id not correct",HttpStatus.NOT_FOUND);
        }
        Category category=products.getCategory();
        Category parent=category.getCategory();
        List<Category>child=categoryRepository.findAllByCategory(parent);// finding just above parent only
        List<Products>similarProduct=new ArrayList<>();
        for(Category c:child){

                List<Products> productsList = c.getProducts();
                addListToList3(productsList, similarProduct,products);

        }
        return similarProduct;
    }

    private void addListToList3(List<Products> productsList, List<Products> similarProduct,
                                Products products) {
        for(Products p:productsList){
            if(!p.equals(products)&& !p.isDeleted()&& p.isActive()) {// not giving the original product
                similarProduct.add(p);
            }
        }
    }


    private void addListToList2(List<ProductVariation> productVariations,
                                List<ProductVariation> productVariations1) {
        for (ProductVariation p:productVariations1){
            if(p.getCustomerAttributes().size()!=0){
                productVariations.add(p);
            }
        }
    }

    private void addListToList(List<Products> productsList, List<Products> productsList1) {
        for(Products p:productsList1){
            productsList.add(p);
        }
    }

}
