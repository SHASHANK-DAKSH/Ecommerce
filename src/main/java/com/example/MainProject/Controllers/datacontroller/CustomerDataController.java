package com.example.MainProject.Controllers.datacontroller;

import com.example.MainProject.dto.*;
import com.example.MainProject.entities.users.Address;
import com.example.MainProject.service.dataservice.CustomerDataService;
import com.example.MainProject.service.dataservice.Impl.CustomerDataServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerDataController {

    @Autowired
    CustomerDataService customerDataService;


    Logger logger= LoggerFactory.getLogger(CustomerDataController.class);
    @GetMapping("/customer/profile")
    public ResponseEntity<GetCustomerResponseDto > getCustomerData(@RequestHeader String token) {
        logger.info("service layer called");
        GetCustomerResponseDto dto=customerDataService.getCustomer(token);
        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }

    @PutMapping("/customer/update")
    public ResponseEntity<GenerateResponse> updateCustomer(@RequestHeader String token,
                                                           @Valid @RequestBody CustomerUpdateDto customerUpdateDto) {
        logger.info("service layer called");
        GenerateResponse message=customerDataService.updateCustomer(token, customerUpdateDto);
        return new ResponseEntity<>(message,HttpStatus.CREATED);
    }

    @PutMapping("/customer/password")//making use of forgotPassword Dto to update
    public ResponseEntity<GenerateResponse> updatePassword(@RequestHeader String token, @Valid @RequestBody ForgotPasswordDto
            forgotPasswordDto) {
        logger.info("service layer called");
        GenerateResponse message=customerDataService.updateCustomerPassword(token, forgotPasswordDto);
        return new ResponseEntity<>(message,HttpStatus.CREATED);
    }

    @PostMapping("/customer/address")
    public ResponseEntity<GenerateResponse> AddAddress(@RequestHeader String token,
                                                        @Valid @RequestBody AddressDto addressDto) {
        logger.info("service layer called");
        GenerateResponse message= customerDataService.addAddress(token, addressDto);
        return new ResponseEntity<>(message,HttpStatus.CREATED);
    }

    @GetMapping("/customer/view-address")
    public ResponseEntity<List<Address>> viewAddress(@RequestHeader String token) {
        logger.info("service layer called");
        List<Address>data= customerDataService.viewAddress(token);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    @DeleteMapping("/customer/delete-address/{id}")
    public ResponseEntity<GenerateResponse> deleteAddress(@RequestHeader String token, @PathVariable Long id) {
        logger.info("service layer called");
        GenerateResponse message= customerDataService.deleteAddress(token, id);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @PatchMapping("/customer/update-address/{id}")
    public ResponseEntity<GenerateResponse> updateAddress
            (@RequestHeader String token, @Valid @RequestBody SellerAddressUpdateDto sellerAddressUpdateDto,
             @PathVariable Long id) {
        logger.info("service layer called");
        GenerateResponse message= customerDataService.updateAddress(token, sellerAddressUpdateDto, id);
        return new ResponseEntity<>(message,HttpStatus.CREATED);
    }

    @PutMapping("/customer/upload-image")
    public ResponseEntity<GenerateResponse> imageUpload(@RequestHeader String token,
                                                        @RequestParam MultipartFile file) throws IOException {
        logger.info("service layer called");
        GenerateResponse message=customerDataService.imageUpload(token, file);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

}
