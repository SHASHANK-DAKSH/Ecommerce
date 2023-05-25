package com.example.MainProject.Controllers.datacontroller;

import com.example.MainProject.dto.*;
import com.example.MainProject.service.dataservice.Impl.SellerDataServiceImpl;
import com.example.MainProject.service.dataservice.SellerDataService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/")
public class SellerDataController {

    @Autowired
    SellerDataServiceImpl sellerDataService;

    Logger logger = LoggerFactory.getLogger(SellerDataController.class);

    @GetMapping("/seller/data")
    public ResponseEntity<SellerDataListDto> getSellerData(@RequestHeader String token) {
        logger.info("calling service layer");
        SellerDataListDto data = sellerDataService.getSellerData(token);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PatchMapping("/seller/update")
    public ResponseEntity<?> upDateSeller(@RequestHeader String token, @Valid @RequestBody
    SellerUpdateDto sellerUpdateDto) {
        logger.info("calling service layer");
        GenerateResponse message = sellerDataService.upDateSeller(token, sellerUpdateDto);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PatchMapping("/seller/password")
    public ResponseEntity<GenerateResponse> upDatePassword(@RequestHeader String token,
                                                           @RequestBody @Valid ForgotPasswordDto forgotPasswordDto) {
        logger.info("calling service layer");
        GenerateResponse message = sellerDataService.upDatePassword(token, forgotPasswordDto);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PatchMapping("/seller/address/{id}")
    public ResponseEntity<GenerateResponse> upDateAddress(@RequestHeader String token, @PathVariable Long id,
                                                          @Valid @RequestBody SellerAddressUpdateDto sellerAddressUpdateDto) {
        logger.info("calling service layer");
        GenerateResponse message = sellerDataService.upDateAddress(token, id, sellerAddressUpdateDto);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PutMapping("/seller/upload-image")
    public ResponseEntity<?> imageUpload(@RequestHeader String token,
                                         @RequestParam MultipartFile file) throws IOException {
        logger.info("calling service layer");
        return sellerDataService.imageUpload(token, file);
    }

}
