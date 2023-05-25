package com.example.MainProject.dto;

import com.example.MainProject.Converter.HashMapConverter;
import jakarta.persistence.Convert;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductVariationResponse {

    private Long productVariationId;
    private String productName;
    @Convert(converter = HashMapConverter.class)
    private Map<String,String> metadata;

    private Integer quantity;

    private long price;
}
