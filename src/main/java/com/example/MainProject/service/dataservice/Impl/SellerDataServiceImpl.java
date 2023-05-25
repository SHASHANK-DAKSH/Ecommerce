package com.example.MainProject.service.dataservice.Impl;

import com.example.MainProject.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SellerDataServiceImpl {

    public SellerDataListDto getSellerData(String token);

    public GenerateResponse upDateSeller(String token, SellerUpdateDto sellerUpdateDto);

    public GenerateResponse upDatePassword(String token, ForgotPasswordDto forgotPasswordDto);

    public GenerateResponse upDateAddress(String token, Long id, SellerAddressUpdateDto sellerAddressUpdateDto);

    public ResponseEntity<?> imageUpload(String token , MultipartFile file) throws IOException;
}
