package com.example.MainProject.service.categories;

import com.example.MainProject.dto.*;
import com.example.MainProject.entities.categories.Category;
import com.example.MainProject.entities.categories.CategoryMetaDataField;
import com.example.MainProject.entities.categories.CategoryMetaDataFieldValues;
import com.example.MainProject.entities.categories.CategoryMetaDataLink;
import com.example.MainProject.exception.CategoryException;
import com.example.MainProject.exception.GenericException;
import com.example.MainProject.repository.CategoryMetaDataFieldRepository;
import com.example.MainProject.repository.CategoryMetaDataFieldValuesRepository;
import com.example.MainProject.repository.CategoryRepository;
import com.example.MainProject.service.categories.Impl.AdminCategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminCategoryService implements AdminCategoryServiceImpl {
    @Autowired
    CategoryMetaDataFieldRepository categoryMetaDataFieldRepository;

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryMetaDataFieldValuesRepository categoryMetaDataFieldValuesRepository;


    @Override
    public GenerateResponse addMetaData(CategoryMetadataFieldDto categoryMetadataFieldDto) {
        if(categoryMetaDataFieldRepository.existsByNameIgnoreCase(categoryMetadataFieldDto.getField())){
            throw new CategoryException("field already exist",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        CategoryMetaDataField categoryMetaDataField=new CategoryMetaDataField();
        categoryMetaDataField.setName(categoryMetadataFieldDto.getField());
        categoryMetaDataFieldRepository.save(categoryMetaDataField);
        return new GenerateResponse("field added with ID"+" "
                +categoryMetaDataField.getId());
    }

    @Override
    public List<CategoryMetadataFieldDto> showMetaDataField() {

        List<CategoryMetaDataField>data=categoryMetaDataFieldRepository.findAll();
        List<CategoryMetadataFieldDto>showData=new ArrayList<>();

        if(data.size()==0){
            throw new CategoryException("data size is 0",HttpStatus.INTERNAL_SERVER_ERROR);
        }

            for (CategoryMetaDataField c : data) {
                CategoryMetadataFieldDto categoryMetadataFieldDto = new CategoryMetadataFieldDto();
                categoryMetadataFieldDto.setField(c.getName());
                showData.add(categoryMetadataFieldDto);
            }
            return showData;

    }

@Override
   public GenerateResponse addCategory(CategoryDto categoryDTO) {

    GenerateResponse message=new GenerateResponse();
    if(categoryDTO.getParent()==null){
        if(!categoryRepository.existsByNameIgnoreCase(categoryDTO.getName())){
            Category category1 = new Category();
            category1.setName(categoryDTO.getName());
            categoryRepository.save(category1);
            message.setMessage("Category added.");
            return message;
        }
        throw new CategoryException("Category is already present.",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    Category parentNode = categoryRepository.findById(categoryDTO.getParent()).orElse(null);
    if(Objects.isNull(parentNode)){
        throw new CategoryException("Parent did not exist.",HttpStatus.NOT_FOUND);
    }
    if(parentNode.getProducts().size()!=0){
        throw new CategoryException("Parent category is associated with a product.",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    List<Category> allNeighbors = categoryRepository.findAllByCategory(parentNode);
    for(Category category:allNeighbors){//checking at width
        if(category.getName().equals(categoryDTO.getName())){
            throw new CategoryException("Category already exists",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    while (!Objects.isNull(parentNode.getCategory())) {//checking with self and depth
        if(parentNode.getName().equalsIgnoreCase(categoryDTO.getName())){
            throw new CategoryException("Category already exists",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        parentNode = categoryRepository.findById(parentNode.getCategory().getId()).orElse(null);
    }
    if(parentNode.getName().equalsIgnoreCase(categoryDTO.getName())){//checking with root node
        throw new CategoryException("Category already exists",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    Category category = new Category();
    category.setName(categoryDTO.getName());
    category.setCategory(categoryRepository.
            findById(categoryDTO.getParent()).orElse(null));
    categoryRepository.save(category);
    message.setMessage("Category added.");
    return message;
}

    @Override
    public CategoryResponseViewDto getCategoryData(Long id){
        CategoryResponseViewDto categoryResponseViewDto=new CategoryResponseViewDto();
        if(!categoryRepository.existsById(id)){
            throw new CategoryException("enter valid Id",HttpStatus.NOT_FOUND);
        }
        Category node=categoryRepository.findById(id).orElse(null);
        List<Category>child=categoryRepository.findAllByCategory(node);
        categoryResponseViewDto.setNode(node);
        categoryResponseViewDto.setChild(child);
        List<Object>parents=new ArrayList<>();// taken object so that i can mention whose parent is whom line100
        while(!Objects.isNull(node.getCategory())){
            parents.add(0,node.getCategory());
            parents.add(0,"PARENT OF "+node.getName());
            node=categoryRepository.findById(node.getCategory().getId()).orElse(null);

        }
        categoryResponseViewDto.setParent(parents);
        return categoryResponseViewDto;

    }
    @Override
    public List<Category>getAllCategoryData(){
        List<Category>allCategory=categoryRepository.findAll();
        return allCategory;
    }

    @Override
    public GenerateResponse updateCategory(Long id, CategoryMetadataFieldDto categoryMetadataFieldDto) {
        if(!categoryRepository.existsById(id)){
            throw new CategoryException("Not A valid ID",HttpStatus.NOT_FOUND);
        }
        if(categoryRepository.existsByNameIgnoreCase(categoryMetadataFieldDto.getField())){
            throw new CategoryException("field name already exists",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Category category=categoryRepository.findById(id).orElse(null);

        category.setName(categoryMetadataFieldDto.getField());
       categoryRepository.save(category);
       return new GenerateResponse("Category updated");

    }
    @Override
    public GenerateResponse addMetaDataFieldValues(CategoryMetaFieldValueDto categoryMetaFieldValueDto){
        if(!categoryRepository.existsById(categoryMetaFieldValueDto.getCategoryId())){
            throw new CategoryException("Not a valid Customer Id",
                    HttpStatus.NOT_FOUND);
        }
        if(!categoryMetaDataFieldRepository.existsById(categoryMetaFieldValueDto.getMetaDataId())){
            throw new CategoryException("not a valid METADATA ID",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        CategoryMetaDataFieldValues categoryMetaDataFieldValues=new CategoryMetaDataFieldValues();//table in which data is to be set

        Category category=categoryRepository.findById(categoryMetaFieldValueDto.getCategoryId())
                .orElse(null);
       if(categoryRepository.existsByCategory(category)){//checking if category is leaf
          throw new GenericException("not a leaf category",HttpStatus.INTERNAL_SERVER_ERROR);
       }
        CategoryMetaDataField categoryMetaDataField=categoryMetaDataFieldRepository.findById(
                categoryMetaFieldValueDto.getMetaDataId()).orElse(null);

        List<String>data=categoryMetaFieldValueDto.getMetaValues();
        List<String>newData=data.stream().filter(i-> Collections.frequency(data,i)>1)
                        .toList();

        if(newData.size()!=0){
            throw new CategoryException("Values cant have duplicate ",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        categoryMetaDataFieldValues.setCategory(category);
        categoryMetaDataFieldValues.setCategoryMetaDataField(categoryMetaDataField);
        categoryMetaDataFieldValues.setValues(data);

        categoryMetaDataFieldValuesRepository.save(categoryMetaDataFieldValues);

        return new GenerateResponse("categoryMeta Data field mapped");


    }

    @Override
    public GenerateResponse updateMetaData(CategoryMetaFieldValueDto categoryMetaFieldValueDto) {
        CategoryMetaDataLink categoryMetaDataLink=new CategoryMetaDataLink();
        categoryMetaDataLink.setCategoryId(categoryMetaFieldValueDto.getCategoryId());
        categoryMetaDataLink.setCategoryMetaDataFieldId
                (categoryMetaFieldValueDto.getMetaDataId());
        CategoryMetaDataFieldValues categoryMetaDataFieldValues=categoryMetaDataFieldValuesRepository.
                findByCategoryMetaDataLink(categoryMetaDataLink).orElse(null);
        if(Objects.isNull(categoryMetaDataFieldValues)){
            throw new GenericException("not a valid combination",HttpStatus.NOT_FOUND);
        }
        List<String>data=categoryMetaDataFieldValues.getValues();
        Set<String>valueset=new HashSet<>(data);
        List<String>actual=new ArrayList<>();
        actual.addAll(data);//old values
        List<String>dto=categoryMetaFieldValueDto.getMetaValues();
        for(String val:dto){
            if(!valueset.contains(val)){
                actual.add(val);
            }
        }
        categoryMetaDataFieldValues.setValues(actual);

        categoryMetaDataFieldValuesRepository.save(categoryMetaDataFieldValues);
        return new GenerateResponse("field updated");

    }

}




































//    @Override
//    public GenerateResponse addCategory(CategoryDto categoryDto) {
//
//       if(categoryRepository.existsByNameIgnoreCase(categoryDto.getName())){
//           throw new CategoryException("category already exist",HttpStatus.CONFLICT);
//       }
//        Category category=new Category();
//       category.setName(categoryDto.getName());//setting category first
//        if(!Objects.isNull(categoryDto.getParentName())){// checking if user added parent or not,if not then add as parent
//            Category parent=categoryRepository.findByNameIgnoreCase(categoryDto.getParentName()).orElse(null);// checking parent
//            if(Objects.isNull(parent)){// checking if entered parent is valid or not
//                // if parent category is not correct then dont added the category
//                throw  new CategoryException("enter valid parent category",HttpStatus.CONFLICT);
//            }
//            if(parent.getProducts().size()!=0){
//                throw new CategoryException("this parent have products so it cant become a parent node",
//                        HttpStatus.CONFLICT);
//            }
//            category.setCategory(parent);
//        }
//
//       categoryRepository.save(category);
//
//       return new GenerateResponse("new Category added");
//
//    }
