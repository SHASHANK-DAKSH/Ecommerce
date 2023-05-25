package com.example.MainProject.service.productservice.Impl;

import com.example.MainProject.dto.AdminProductDto;
import com.example.MainProject.dto.AdminProductResponseDto;
import com.example.MainProject.dto.GenerateResponse;

import java.util.List;

public interface AdminProductServiceImpl {

    public GenerateResponse activateProduct(Long id);

    public GenerateResponse DeactivateProduct(Long id);

    public AdminProductDto getProductFromId(Long id);

    public List<AdminProductResponseDto> getAllProduct();
}
