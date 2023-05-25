package com.example.MainProject.entities.categories;

import com.example.MainProject.entities.audit.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

//@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class CategoryMetaDataFieldValues extends Auditable {
    @EmbeddedId
    private CategoryMetaDataLink categoryMetaDataLink = new CategoryMetaDataLink();

    @JsonIgnore
    @ManyToOne
    @MapsId("categoryId")// map pk from category  to categoryId
    Category category;

    @JsonIgnore
    @ManyToOne
    @MapsId("categoryMetaDataFieldId") // map pk from CMDF to categoryMetaDataFieldId
    CategoryMetaDataField categoryMetaDataField;

    @Column(name="valuesField")
    @Convert(converter = Values.class)
    private List<String>values;
}
