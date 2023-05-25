package com.example.MainProject.entities.categories;

import com.example.MainProject.entities.audit.Auditable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class CategoryMetaDataField extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "categoryMetaDataField",cascade = CascadeType.ALL,orphanRemoval = true)
    List<CategoryMetaDataFieldValues> categoryMetaDataFieldValuesList;


}
