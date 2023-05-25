package com.example.MainProject.Controllers.categorycontroller;

import com.example.MainProject.Controllers.datacontroller.AdminDataController;
import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.service.categories.CustomerCategoryService;
import com.example.MainProject.service.categories.Impl.CustomerCategoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/")
public class CustomerCategoryController {


    @Autowired
    CustomerCategoryServiceImpl customerCategoryService;

    Logger logger = LoggerFactory.getLogger(CustomerCategoryController.class);


    @GetMapping("/customer/customer-category/")
    public ResponseEntity<List<Category>>getCustomerData(@RequestParam(required = false)Long id){
        logger.info("category service called");
        List<Category>data= customerCategoryService.getCustomerData(id);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
