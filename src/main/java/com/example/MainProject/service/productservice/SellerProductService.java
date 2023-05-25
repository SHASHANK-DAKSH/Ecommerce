package com.example.MainProject.service.productservice;

import com.example.MainProject.dto.*;
import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.entities.categories.CategoryMetaDataField;
import com.example.MainProject.entities.categories.CategoryMetaDataFieldValues;
import com.example.MainProject.entities.product.ProductVariation;
import com.example.MainProject.entities.product.Products;
import com.example.MainProject.entities.users.Seller;
import com.example.MainProject.exception.GenericException;
import com.example.MainProject.exception.ProductException;
import com.example.MainProject.exception.SellerException;
import com.example.MainProject.repository.*;
import com.example.MainProject.security.JwtGenerator;
import com.example.MainProject.service.productservice.Impl.SellerProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class SellerProductService implements SellerProductServiceImpl {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    JwtGenerator jwtGenerator;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    ProductsRepo productsRepo;
    @Autowired
    CategoryMetaDataFieldRepository categoryMetaDataFieldRepository;
    @Autowired
    CategoryMetaDataFieldValuesRepository categoryMetaDataFieldValuesRepository;
    @Autowired
    ProductVariationRepo productVariationRepo;

    String base_path="/home/shashank/Shashank_Daksh_6980/MainProject/src/" +
            "main/java/com/example/MainProject/Images/productVariationImage/";

    @Override
    public GenerateResponse addProduct(ProductDto productDto,HttpServletRequest httpServletRequest) {
        String token=httpServletRequest.getHeader("Authorization").substring(7);
        Category category = categoryRepository.findById(productDto.getId()).orElse(null);
        if(Objects.isNull(category)){
            throw new GenericException("categoryId not valid", HttpStatus.NOT_FOUND);
        }
        if(categoryRepository.existsByCategory(category)){
            throw new GenericException("Category is not a leaf category",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String email=jwtGenerator.getUserNameFromJwt(token);
        Seller seller=sellerRepository.findByEmail(email).orElse(null);
        if(!seller.isActive()){
            throw new GenericException("seller not active",HttpStatus.UNAUTHORIZED);
        }
        Products newproducts=new Products();

        List<Products>productsList=productsRepo.findAllByName(productDto.getName());
        // exist in db

        if(productsList.size()==0){
            newproducts.setName(productDto.getName());
            newproducts.setSeller(seller);
            newproducts.setCategory(category);
            newproducts.setBrand(productDto.getBrand());
            newproducts.setReturnAble(productDto.isReturnable());
            newproducts.setCancellable(productDto.isCancelable());
            newproducts.setDescription(productDto.getDescription());
            productsRepo.save(newproducts);
            return new GenerateResponse("product added successfully");
        }

        for(Products products1:productsList) {
            if (products1.getName().equalsIgnoreCase(productDto.getName()) && products1.getSeller().equals(seller)
                    && products1.getCategory().equals(category) && products1.getBrand().equals(productDto.getBrand())) {
                throw new GenericException("not a unique combination", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        newproducts.setName(productDto.getName());
        newproducts.setSeller(seller);
        newproducts.setCategory(category);
        newproducts.setBrand(productDto.getBrand());
        newproducts.setReturnAble(productDto.isReturnable());
        newproducts.setCancellable(productDto.isCancelable());
        newproducts.setDescription(productDto.getDescription());
        productsRepo.save(newproducts);
        return new GenerateResponse("product added successfully");
    }

    @Override
    public GenerateResponse addVariation(ProductVariationDto productVariationDto,HttpServletRequest httpServletRequest){
        String token=httpServletRequest.getHeader("Authorization").substring(7);
        String email= jwtGenerator.getUserNameFromJwt(token);
        Seller seller=sellerRepository.findByEmail(email).orElse(null);
        if(!productsRepo.existsById(productVariationDto.getProductId())){
            throw new GenericException("not a id",HttpStatus.NOT_FOUND);
        }
        if(productVariationDto.getQuantity()<0){
            throw new GenericException("Quantity is less than 0",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(productVariationDto.getPrice()<0){
            throw new GenericException("Price is less than or equal to 0",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Products products=productsRepo.findById(productVariationDto.getProductId()).get();
        if(!products.isActive()&& products.isDeleted()){//?
            throw new GenericException("product is deleted or not Active",HttpStatus.UNAUTHORIZED);
        }
        Products products1=productsRepo.findById(productVariationDto.getProductId()).get();
        if(seller.getId()!=products1.getSeller().getId()){
            throw new SellerException("seller is not the owner of product do it cant add the varaition",
                    HttpStatus.CONFLICT);
        }
        Map<String,String>metaData=productVariationDto.getMetadata();
        Map<String,String>data=new HashMap<>();

        for(Map.Entry<String,String>m:metaData.entrySet()){
            //checking if metadataField exists
            if(!categoryMetaDataFieldRepository.existsByNameIgnoreCase(m.getKey())){
                throw new GenericException("Field value doesn't exists",HttpStatus.NOT_FOUND);
            }
            CategoryMetaDataField categoryMetaDataField=categoryMetaDataFieldRepository.findByName(m.getKey()).get();
            //checking if metadataField and value exist for given categoryId and product.
            if(!categoryMetaDataFieldValuesRepository.existsByCategoryIdAndCategoryMetaDataFieldId(
                    products.getCategory().getId(),categoryMetaDataField.getId())){
                throw new GenericException("Field value don't exists",HttpStatus.NOT_FOUND);
            }
            CategoryMetaDataFieldValues categoryMetaDataFieldValues=categoryMetaDataFieldValuesRepository.
                    findByCategoryIdAndCategoryMetaDataFieldId(products.getCategory().getId(),categoryMetaDataField.
                            getId()).get();
            List<String>strings=categoryMetaDataFieldValues.getValues();// mark
            for(String s:strings){
                if(s.equalsIgnoreCase(m.getValue())){
                    data.put(m.getKey(),s);
                    break;
                }
            }
        }
        ProductVariation productVariation=new ProductVariation();
        productVariation.setProducts(products);
        productVariation.setPrice(productVariationDto.getPrice());
        productVariation.setCustomerAttributes(data);
        productVariation.setQuantityAvailable(productVariationDto.getQuantity());
        productVariationRepo.save(productVariation);

        return new GenerateResponse("variation added");
    }

    @Override
    public ProductResponseDto getProduct(Long id, HttpServletRequest httpServletRequest) {
        String token=httpServletRequest.getHeader("Authorization").substring(7);
        Products products=productsRepo.findById(id).orElse(null);
        if(Objects.isNull(products)){
            throw new ProductException("product not found enter valid id",HttpStatus.NOT_FOUND);
        }

        String email= jwtGenerator.getUserNameFromJwt(token);

        Seller seller=sellerRepository.findByEmail(email).orElse(null);


        if(!products.getSeller().equals(seller)){
            throw new ProductException("seller is not a owner of the product",HttpStatus.UNAUTHORIZED);
        }
        if(products.isDeleted()){
            throw new ProductException("product is deleted",HttpStatus.UNAUTHORIZED);
        }
        ProductResponseDto productResponseDto=new ProductResponseDto();
        productResponseDto.setId(products.getId());
        productResponseDto.setBrand(products.getBrand());
        productResponseDto.setName(products.getName());
        productResponseDto.setCategory(products.getCategory());
        return productResponseDto;
    }

    @Override
    public ProductVariationResponse getParticularVariation(Long id,HttpServletRequest httpServletRequest){
        ProductVariation productVariation=productVariationRepo.findById(id).orElse(null);
        if(Objects.isNull(productVariation)){
            throw new ProductException("Variation Id not valid",HttpStatus.NOT_FOUND);
        }
        String token=httpServletRequest.getHeader("Authorization").substring(7);
        String email=jwtGenerator.getUserNameFromJwt(token);
        Seller authSeller=sellerRepository.findByEmail(email).get();
        Products products=productsRepo.findById(productVariation.getProducts().getId()).get();
        Seller seller=sellerRepository.findById(products.getSeller().getId()).get();
        if(authSeller.getId()!=seller.getId()){
            throw new GenericException("the seller is not the owner of variation",HttpStatus.UNAUTHORIZED);
        }
        if(productVariation.getProducts().isDeleted()){
            throw new GenericException("Product is deleted",HttpStatus.UNAUTHORIZED);
        }
        ProductVariationResponse response=new ProductVariationResponse();
        response.setProductVariationId(productVariation.getId());
        response.setProductName(productVariation.getProducts().getName());
        response.setQuantity(productVariation.getQuantityAvailable());
        response.setPrice(productVariation.getPrice());
        response.setMetadata(productVariation.getCustomerAttributes());

        return response;

    }
    @Override
    public List<ProductResponseDto> allProduct(HttpServletRequest httpServletRequest) {
        String token =httpServletRequest.getHeader("Authorization").substring(7);
        String email=jwtGenerator.getUserNameFromJwt(token);
        Seller seller=sellerRepository.findByEmail(email).orElse(null);
        List<Products>productsList=productsRepo.findAll();
        List<Products>actual=new ArrayList<>();
        for(Products p:productsList){
            if(!p.isDeleted()){
                actual.add(p);
            }
        }
        List<ProductResponseDto>ans=new ArrayList<>();
        for(Products p:actual){
            if(p.getSeller().equals(seller)) {// checking if the user is owner of the product
                ProductResponseDto product = new ProductResponseDto();
                product.setId(p.getId());
                product.setName(p.getName());
                product.setCategory(p.getCategory());
                product.setBrand(p.getBrand());
                ans.add(product);
            }
        }
        return ans;
    }
    @Override
    public GenerateResponse deleteProduct(Long id,HttpServletRequest httpServletRequest) {
        String token=httpServletRequest.getHeader("Authorization").substring(7);

        Products products=productsRepo.findById(id).orElse(null);
        if(Objects.isNull(products)){
            throw new GenericException("not a valid product Id",HttpStatus.NOT_FOUND);
        }
        String email=jwtGenerator.getUserNameFromJwt(token);
        Seller seller=sellerRepository.findByEmail(email).orElse(null);
        if(!products.getSeller().equals(seller)){
            throw new ProductException("seller is not owner of product",HttpStatus.UNAUTHORIZED);
        }

        if(products.isDeleted()){
            throw new ProductException("Product already deleted",HttpStatus.UNAUTHORIZED);
        }
        products.setDeleted(true);
//        productsRepo.deleteById(products.getId());
        productsRepo.save(products);
        return new GenerateResponse("product Deleted");

    }

    @Override
    public GenerateResponse updateProduct(Long id, ProductUpdateDto productUpdateDto,
                                          HttpServletRequest httpServletRequest) {
        String token=httpServletRequest.getHeader("Authorization").substring(7);
        Products products=productsRepo.findById(id).orElse(null);
        if(Objects.isNull(products)){
            throw new GenericException("not a valid Id",HttpStatus.NOT_FOUND);
        }

        String email=jwtGenerator.getUserNameFromJwt(token);
        Seller seller=sellerRepository.findByEmail(email).orElse(null);

        if(!products.getSeller().equals(seller)){
            throw new GenericException("Seller is not owner of the Product",HttpStatus.UNAUTHORIZED);
        }
        //Category category=categoryRepository.findById(products.getCategory().getId()).orElse(null);
//        if(productsRepo.existsByName(productUpdateDto.getName())){
//            throw new GenericException("Product already exist",HttpStatus.CONFLICT);
//        }
        products.setName(productUpdateDto.getName());
        products.setCancellable(productUpdateDto.isCancelable());
        products.setReturnAble(productUpdateDto.isReturnable());
        products.setDescription(productUpdateDto.getDescription());
        productsRepo.save(products);
        return new GenerateResponse("Product Updated");

    }

    @Override
    public List<ProductVariationResponse> getAllVariation(Long id,HttpServletRequest httpServletRequest){
        if(!productsRepo.existsById(id)){
            throw new ProductException("Product not found",HttpStatus.NOT_FOUND);
        }
        Products products=productsRepo.findById(id).get();
        String token=httpServletRequest.getHeader("Authorization").substring(7);
        String email=jwtGenerator.getUserNameFromJwt(token);
        Seller authseller=sellerRepository.findByEmail(email).orElse(null);
        Seller seller=sellerRepository.findById(products.getSeller().getId()).get();

        if(authseller.getId()!=seller.getId()){
            throw new GenericException("token is not of the given product seller ",HttpStatus.FORBIDDEN);
        }
        if(products.isDeleted()){
            throw new ProductException("Product is deleted",HttpStatus.UNAUTHORIZED);
        }
        List<ProductVariation>list=productVariationRepo.findByProducts(products).orElse(null);
        if(list.size()==0){
            throw new ProductException("product don't have any variation",HttpStatus.UNAUTHORIZED);
        }
        List<ProductVariationResponse>responseList=new ArrayList<>();
        for(ProductVariation pv:list){

            ProductVariationResponse response=new ProductVariationResponse();
            response.setProductVariationId(pv.getId());
            response.setPrice(pv.getPrice());
            response.setQuantity(pv.getQuantityAvailable());
            response.setMetadata(pv.getCustomerAttributes());
            response.setProductName(products.getName());
            responseList.add(response);
        }
        return responseList;

    }
    @Override
    public GenerateResponse updateProductVariation(Long id,UpdateProductVariationDto dto,
                                                   HttpServletRequest httpServletRequest){
        String token=httpServletRequest.getHeader("Authorization").substring(7);
        ProductVariation productVariation=productVariationRepo.findById(id).orElse(null);
        if(Objects.isNull(productVariation)){
            throw new ProductException("Variation id not correct",HttpStatus.NOT_FOUND);
        }
        String email= jwtGenerator.getUserNameFromJwt(token);
        Seller seller=sellerRepository.findByEmail(email).orElse(null);
        if(Objects.isNull(seller)){
            throw new ProductException("seller is not found for token",HttpStatus.NOT_FOUND);
        }
        if(seller.getId()!=productVariation.getProducts().getSeller().getId()){
            throw new ProductException("seller is not the owner of product",HttpStatus.FORBIDDEN);
        }

        productVariation.setQuantityAvailable(dto.getQuantity());
        productVariation.setPrice(dto.getPrice());
        productVariation.setIsActive(dto.isActive());
        productVariationRepo.save(productVariation);
        return new GenerateResponse("variation updated");
    }

    @Override
    public GenerateResponse addVariationImage
            (Long id, MultipartFile file,HttpServletRequest httpServletRequest)throws IOException {
        String token=httpServletRequest.getHeader("Authorization").substring(7);
        String email=jwtGenerator.getUserNameFromJwt(token);
        Seller seller=sellerRepository.findByEmail(email).orElse(null);
        ProductVariation productVariation=productVariationRepo.findById(id).orElse(null);
        if(Objects.isNull(productVariation)){
            throw new GenericException("Product Variation not found",HttpStatus.NOT_FOUND);
        }
        Products products=productVariation.getProducts();
        if(!products.getSeller().equals(seller)){
            throw new ProductException("Seller is not owner of product",HttpStatus.FORBIDDEN);
        }
        productVariation.setImage(base_path+file.getOriginalFilename());
        productVariationRepo.save(productVariation);
        file.transferTo(new File(base_path+file.getOriginalFilename()));
        return new GenerateResponse("Image Added");
    }
}