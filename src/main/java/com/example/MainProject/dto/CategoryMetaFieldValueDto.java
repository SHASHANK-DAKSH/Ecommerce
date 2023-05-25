package com.example.MainProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryMetaFieldValueDto {

    @NotNull(message = "categoryID cant be blank")
    private Long categoryId;

    @NotNull(message = "MetaDataID cant be blank")
    private Long metaDataId;
    @Size(min = 2,message = "size of list must be atLeast 1")
    private List<String> metaValues;
}