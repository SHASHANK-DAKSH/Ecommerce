package com.example.MainProject.Controllers.authcontroller;

import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.dto.MessageDto;
import com.example.MainProject.dto.SellerDto;
import com.example.MainProject.service.authservice.Impl.SellerServiceImpl;
import com.example.MainProject.service.authservice.SellerService;
import jakarta.validation.Valid;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Data
public class SellerController {
    @Autowired
    SellerServiceImpl  sellerService;

    Logger logger= LoggerFactory.getLogger(SellerController.class);


    @PostMapping("/seller/register")
    public ResponseEntity<GenerateResponse>getSeller(@Valid @RequestBody SellerDto sellerDto) {
        logger.info("seller service called for adding seller data");
        GenerateResponse message= sellerService.getSellerDetails(sellerDto);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }



}
