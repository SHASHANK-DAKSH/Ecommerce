package com.example.MainProject.Controllers.productcontroller;

import com.example.MainProject.Controllers.datacontroller.AdminDataController;
import com.example.MainProject.dto.AdminProductDto;
import com.example.MainProject.dto.AdminProductResponseDto;
import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.dto.ProductResponseDto;
import com.example.MainProject.entities.product.Products;
import com.example.MainProject.service.productservice.AdminProductService;
import com.example.MainProject.service.productservice.Impl.AdminProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminProductController {
    @Autowired
    AdminProductServiceImpl adminProductService;

    Logger logger = LoggerFactory.getLogger(AdminProductController.class);


    @PutMapping("/admin/activate-product/{id}")
    public ResponseEntity<?>activateProduct(@PathVariable Long id){
        logger.info("product service called");
       GenerateResponse message=adminProductService.activateProduct(id);
       return new ResponseEntity<>(message, HttpStatus.CREATED);
   }
    @PutMapping("/admin/deactivate-product/{id}")
    public ResponseEntity<GenerateResponse>deactivateProduct(@PathVariable Long id){
        logger.info("product service called");
        GenerateResponse message=adminProductService.DeactivateProduct(id);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
    @GetMapping("/admin/product-details/{id}")
    public ResponseEntity<AdminProductDto>getProductFromId(@PathVariable Long id){
        logger.info("product service called");
       AdminProductDto response=adminProductService.getProductFromId(id);
       return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/admin/all-product-data")
    public ResponseEntity<List<AdminProductResponseDto>>getAllProductsData(){
        logger.info("product service called");
       List<AdminProductResponseDto>productsList=adminProductService.getAllProduct();
       return new ResponseEntity<>(productsList,HttpStatus.OK);
    }
}
