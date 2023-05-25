package com.example.MainProject.Controllers.productcontroller;

import com.example.MainProject.dto.*;
import com.example.MainProject.service.productservice.Impl.SellerProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("api/")
public class SellerProductController {

    @Autowired
    SellerProductServiceImpl sellerProductService;

    Logger logger = LoggerFactory.getLogger(AdminProductController.class);


    @PostMapping("/seller/product-addition")
    public ResponseEntity<GenerateResponse> addProduct(@Valid @RequestBody ProductDto productDto,
                                                       HttpServletRequest httpServletRequest) {
        logger.info("product service called for seller");
        GenerateResponse message = sellerProductService.addProduct(productDto, httpServletRequest);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/seller/addProduct-variation")
    public ResponseEntity<GenerateResponse> addVariation(@Valid @RequestBody ProductVariationDto productVariationDto,
                                                         HttpServletRequest httpServletRequest) {
        // adding servet to make only product owner add the variation
        logger.info("product service called for seller");
        GenerateResponse message = sellerProductService.addVariation(productVariationDto, httpServletRequest);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/seller/product-view/{id}")
    public ResponseEntity<ProductResponseDto> viewProduct(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        logger.info("product service called for seller");
        ProductResponseDto productResponseDto = sellerProductService.getProduct(id, httpServletRequest);
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    @GetMapping("/seller/product-variation-view/{id}")
    public ResponseEntity<ProductVariationResponse> viewVariation(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        logger.info("product service called for seller");
        ProductVariationResponse productVariationResponse = sellerProductService.getParticularVariation(id,
                httpServletRequest);
        return new ResponseEntity<>(productVariationResponse, HttpStatus.OK);
    }

    @GetMapping("/seller/product-allview")
    public ResponseEntity<List<ProductResponseDto>> viewAllProduct(HttpServletRequest httpServletRequest) {
        logger.info("product service called for seller");
        List<ProductResponseDto> allData = sellerProductService.allProduct(httpServletRequest);
        return new ResponseEntity<>(allData, HttpStatus.OK);
    }

    @GetMapping("/seller/product-variation/view-all/{id}")
    public ResponseEntity<List<ProductVariationResponse>> getAllVariationOfProduct
            (@PathVariable Long id, HttpServletRequest httpServletRequest) {
        logger.info("product service called for seller");
        List<ProductVariationResponse> responseList = sellerProductService.getAllVariation(id, httpServletRequest);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @DeleteMapping("/seller/product-delete/{id}")
    public ResponseEntity<GenerateResponse> deleteProduct(@PathVariable Long id,
                                                          HttpServletRequest httpServletRequest) {
        logger.info("product service called for seller");
        GenerateResponse message = sellerProductService.deleteProduct(id, httpServletRequest);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/seller/update-product/{id}")
    public ResponseEntity<GenerateResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateDto
            productUpdateDto, HttpServletRequest httpServletRequest) {
        logger.info("product service called for seller");
        GenerateResponse message = sellerProductService.updateProduct(id, productUpdateDto, httpServletRequest);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/seller/update-variation/{id}")
    public ResponseEntity<GenerateResponse> updateVariation(@PathVariable Long id, @Valid @RequestBody
    UpdateProductVariationDto updateProductVariationDto,
                                                            HttpServletRequest httpServletRequest) {
        GenerateResponse message = sellerProductService.updateProductVariation(id, updateProductVariationDto,
                httpServletRequest);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/seller/product-variation/image/{id}")
    public ResponseEntity<GenerateResponse> addProductVariationImage(@PathVariable Long id,
                                                                     @RequestParam MultipartFile file,
                                                                     HttpServletRequest httpServletRequest)
            throws IOException {
        logger.info("product service called for seller");
        GenerateResponse message = sellerProductService.addVariationImage(id, file, httpServletRequest);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
}



