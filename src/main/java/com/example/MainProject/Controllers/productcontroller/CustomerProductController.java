package com.example.MainProject.Controllers.productcontroller;

import com.example.MainProject.dto.CategoryProductDto;
import com.example.MainProject.dto.CustomerProductDto;
import com.example.MainProject.entities.product.Products;
import com.example.MainProject.service.productservice.CustomerProductService;
import com.example.MainProject.service.productservice.Impl.CustomerProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerProductController {
    @Autowired
    CustomerProductServiceImpl customerProductService;

    Logger logger = LoggerFactory.getLogger(CustomerProductController.class);

    @GetMapping("/customer/get-product/details/{id}")
    public ResponseEntity<CustomerProductDto>getProductData(@PathVariable Long id){
        logger.info("product service called for customer product");
        CustomerProductDto customer=customerProductService.getProductDetails(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
    @GetMapping("/customer/get-all-product/details/{id}")
    public ResponseEntity<CategoryProductDto>getAllDataOfCategory(@PathVariable Long id){
        logger.info("product service called for customer product");
        CategoryProductDto response=customerProductService.getAllProductFromCategory(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/customer/similar-product/{id}")
    public ResponseEntity<List<Products>>getSimilar(@PathVariable Long id){
        logger.info("product service called for customer product");
        List<Products>data=customerProductService.SimilarProduct(id);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }


}
