package com.example.MainProject.Controllers.datacontroller;


import com.example.MainProject.dto.CustomerResponseDto;
import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.dto.SellerResponseDto;
import com.example.MainProject.service.dataservice.AdminDataService;
import com.example.MainProject.service.dataservice.Impl.AdminDataServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class AdminDataController {

    @Autowired
    AdminDataServiceImpl adminService;

    Logger logger = LoggerFactory.getLogger(AdminDataController.class);

    @GetMapping("/admin/customer")
    public ResponseEntity<List<CustomerResponseDto>> getCustomerData(
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "0") int pageOffSet,
            @RequestParam(required = false, defaultValue = "id") String SortOn) {
        logger.info("calling service layer");
        List<CustomerResponseDto>data= adminService.getCustomerData(pageSize, pageOffSet, SortOn);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/admin/seller")
    public ResponseEntity< List<SellerResponseDto>> getSellerData(
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "0") int pageOffSet,
            @RequestParam(required = false, defaultValue = "id") String SortOn) {
        logger.info("calling service layer");

        List<SellerResponseDto>data= adminService.getSellerData(pageSize, pageOffSet, SortOn);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    @PatchMapping("/admin/activate/{id}")//used for both seller and customer
    public ResponseEntity<GenerateResponse> activateUser(@PathVariable Long id) {
        logger.info("calling service layer");
        GenerateResponse message=adminService.adminActivation(id);
        return new ResponseEntity<>(message,HttpStatus.CREATED);
    }

    @PatchMapping("/admin/deactivate/{id}")////used for both seller and customer
    public ResponseEntity<GenerateResponse> deActivateUser(@PathVariable Long id) {
        logger.info("calling service layer");
        GenerateResponse message= adminService.adminDeActivation(id);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
}
