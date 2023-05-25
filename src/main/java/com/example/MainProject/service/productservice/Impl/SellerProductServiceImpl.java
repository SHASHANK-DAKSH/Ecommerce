package com.example.MainProject.service.productservice.Impl;

import com.example.MainProject.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SellerProductServiceImpl {

    public GenerateResponse addProduct(ProductDto productDto, HttpServletRequest httpServletRequest);

    public GenerateResponse addVariation(ProductVariationDto productVariationDto,HttpServletRequest httpServletRequest);

    public ProductResponseDto getProduct(Long id, HttpServletRequest httpServletRequest);

    public ProductVariationResponse getParticularVariation(Long id, HttpServletRequest httpServletRequest);

    public List<ProductResponseDto> allProduct(HttpServletRequest httpServletRequest);

    public GenerateResponse deleteProduct(Long id,HttpServletRequest httpServletRequest);

    public GenerateResponse updateProduct(Long id, ProductUpdateDto productUpdateDto,
                                          HttpServletRequest httpServletRequest);

    public List<ProductVariationResponse> getAllVariation(Long id,HttpServletRequest httpServletRequest);


    public GenerateResponse updateProductVariation(Long id,UpdateProductVariationDto dto,
                                                   HttpServletRequest httpServletRequest);

    public GenerateResponse addVariationImage
            (Long id, MultipartFile file, HttpServletRequest httpServletRequest)throws IOException;
}
