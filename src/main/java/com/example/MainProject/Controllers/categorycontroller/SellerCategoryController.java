package com.example.MainProject.Controllers.categorycontroller;

import com.example.MainProject.Controllers.datacontroller.AdminDataController;
import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.service.categories.Impl.SellerCategoryServiceImpl;
import com.example.MainProject.service.categories.SellerCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/")
public class SellerCategoryController {

    @Autowired
    SellerCategoryServiceImpl sellerCategoryService;


    Logger logger = LoggerFactory.getLogger(SellerCategoryController.class);

    @GetMapping("/seller/seller-category")
    public ResponseEntity<List<Category>>getSellerData(){
        logger.info("category service called");
        List<Category> data= sellerCategoryService.getSellerCategory();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
