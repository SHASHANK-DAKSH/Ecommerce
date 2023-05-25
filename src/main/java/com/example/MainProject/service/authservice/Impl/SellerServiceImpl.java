package com.example.MainProject.service.authservice.Impl;

import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.dto.MessageDto;
import com.example.MainProject.dto.SellerDto;

public interface SellerServiceImpl {
    public GenerateResponse getSellerDetails(SellerDto sellerDto);
}
