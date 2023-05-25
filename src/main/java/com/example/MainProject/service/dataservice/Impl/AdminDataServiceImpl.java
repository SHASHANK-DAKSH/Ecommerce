package com.example.MainProject.service.dataservice.Impl;

import com.example.MainProject.dto.CustomerResponseDto;
import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.dto.SellerResponseDto;

import java.util.List;

public interface AdminDataServiceImpl {

    public List<CustomerResponseDto> getCustomerData(int pageSize, int pageOffSet, String sortOn);

    public  List<SellerResponseDto> getSellerData(int pageSize, int pageOffSet, String sortOn);

    public GenerateResponse adminActivation(Long id);

    public GenerateResponse adminDeActivation(Long id);
}
