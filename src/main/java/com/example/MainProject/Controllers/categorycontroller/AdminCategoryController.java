package com.example.MainProject.Controllers.categorycontroller;

import com.example.MainProject.Controllers.datacontroller.AdminDataController;
import com.example.MainProject.dto.*;
import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.service.categories.AdminCategoryService;
import com.example.MainProject.service.categories.Impl.AdminCategoryServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
public class AdminCategoryController {

    @Autowired
    AdminCategoryServiceImpl adminCategoryService;

    Logger logger = LoggerFactory.getLogger(AdminCategoryController.class);


    @PostMapping("admin/category/metadataFieldValue")
    public ResponseEntity<GenerateResponse> addMetaDataField
            (@Valid @RequestBody CategoryMetadataFieldDto categoryMetadataFieldDto) {
        logger.info("calling service layer for category");
        GenerateResponse val=adminCategoryService.addMetaData(categoryMetadataFieldDto);
        return new ResponseEntity<>(val, HttpStatus.OK);
    }

    @GetMapping("admin/category/metaDataFieldView")
    public ResponseEntity<List<CategoryMetadataFieldDto>> showMetaDataField(
//            @RequestParam(required = false,defaultValue = "10") Integer pageSize,
//            @RequestParam(required = false,defaultValue = "0") Integer pageOffSet,
//            @RequestParam(required = false) String SortOn
   )
    {// will add optional param later
        logger.info("calling service layer for category");
        List<CategoryMetadataFieldDto>val= adminCategoryService.showMetaDataField();
        return new ResponseEntity<>(val,HttpStatus.OK);
    }

    @PostMapping("admin/category/categoriesAddition")
    public ResponseEntity<GenerateResponse> addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        logger.info("calling service layer for category");
        GenerateResponse val= adminCategoryService.addCategory(categoryDto);
        return new ResponseEntity<>(val,HttpStatus.CREATED);
    }

    @GetMapping("admin/category/viewData/{id}")
    public ResponseEntity<CategoryResponseViewDto> getDataCategory(@PathVariable Long id) {
        logger.info("calling service layer for category");
        CategoryResponseViewDto val= adminCategoryService.getCategoryData(id);
        return new ResponseEntity<>(val,HttpStatus.OK);
    }

    @GetMapping("admin/category/viewAllData/")
    public ResponseEntity<List<Category>> getAllCategory() {
        logger.info("calling service layer for category");
        List<Category>val=adminCategoryService.getAllCategoryData();
        return new ResponseEntity<>(val,HttpStatus.OK);
    }

    @PutMapping("admin/category/upDate/{id}")
    public ResponseEntity<GenerateResponse> upDateCategory(@PathVariable Long id, @Valid @RequestBody
    CategoryMetadataFieldDto categoryMetadataFieldDto) {// using categoryMetadataField since i require only one field of
        //String to update.
        logger.info("calling service layer for category");
        GenerateResponse val=adminCategoryService.updateCategory(id, categoryMetadataFieldDto);
        return new ResponseEntity<>(val,HttpStatus.CREATED);
    }

    @PostMapping("admin/category/metaDataCategory")
    public ResponseEntity<GenerateResponse> mapCategoryWithMetaData( @Valid @RequestBody CategoryMetaFieldValueDto
                                                             categoryMetaFieldValueDto) {
        logger.info("calling service layer for category");
        GenerateResponse val=adminCategoryService.addMetaDataFieldValues(categoryMetaFieldValueDto);
        return new ResponseEntity<>(val,HttpStatus.CREATED);
    }
    @PutMapping("/admin/category/update")
    public ResponseEntity<GenerateResponse>updateCategoryMetadata(
            @Valid @RequestBody CategoryMetaFieldValueDto categoryMetaFieldValueDto){
        logger.info("calling service layer for category");
        GenerateResponse val=adminCategoryService.updateMetaData(categoryMetaFieldValueDto);
        return new ResponseEntity<>(val,HttpStatus.CREATED);
    }

}