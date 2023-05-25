package com.example.MainProject.dto;

import com.example.MainProject.Converter.HashMapConverter;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductVariationDto {
    @NotNull
    private Long productId;
    @Convert(converter = HashMapConverter.class)
    private Map<String,String> metadata;

    @NotNull(message = "give some quantity")

    private Integer quantity;

    @NotNull(message = "give any price")
    private int price;


}
