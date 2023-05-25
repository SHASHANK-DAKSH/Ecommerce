package com.example.MainProject.entities.categories;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@Embeddable
public class CategoryMetaDataLink implements Serializable {

    private Long categoryId;

    private Long categoryMetaDataFieldId;

//    @ManyToOne
//    Category category;
//
//    @ManyToOne
//    CategoryMetaDataField categoryMetaDataField;
}
