package com.example.MainProject.Controllers.authcontroller;

import com.example.MainProject.dto.CustomerDto;
import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.dto.MessageDto;
import com.example.MainProject.service.authservice.CustomerService;
import com.example.MainProject.service.authservice.Impl.CustomerServiceImpl;
import jakarta.validation.Valid;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")

public class CustomerController {
    @Autowired
    CustomerServiceImpl customerService;
    Logger logger= LoggerFactory.getLogger(CustomerController.class);

    @PostMapping("/customer/register")
    public ResponseEntity<MessageDto> getCustomer(@Valid @RequestBody CustomerDto customerDto) {
        logger.info("customer data added by calling customerService");
        MessageDto message=customerService.getCustomerDetails(customerDto);
      return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PutMapping("/customer/activate")
    public ResponseEntity<GenerateResponse> ActivateCustomer(@RequestHeader String token){
        logger.info("Activating added by calling customerService");

        GenerateResponse message=customerService.ActivateToken(token);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
    @PostMapping("/resend")
    public ResponseEntity<GenerateResponse>resendActivate(@RequestHeader String email){
        logger.info("resending activation link by calling customerService");
        GenerateResponse message=customerService.resendLink(email);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

}
