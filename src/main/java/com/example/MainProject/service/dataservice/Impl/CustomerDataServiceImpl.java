package com.example.MainProject.service.dataservice.Impl;

import com.example.MainProject.dto.*;
import com.example.MainProject.entities.users.Address;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CustomerDataServiceImpl {

    public GetCustomerResponseDto getCustomer(String token);

    public GenerateResponse updateCustomer(String token, CustomerUpdateDto customerUpdateDto);

    public GenerateResponse updateCustomerPassword(String token, ForgotPasswordDto forgotPasswordDto);

    public GenerateResponse addAddress(String token, AddressDto addressDto);

    public List<Address> viewAddress(String token);

    public GenerateResponse deleteAddress(String token, Long id);

    public GenerateResponse updateAddress(String token, SellerAddressUpdateDto sellerAddressUpdateDto,Long id);

    public GenerateResponse imageUpload(String token , MultipartFile file)throws IOException;
}
