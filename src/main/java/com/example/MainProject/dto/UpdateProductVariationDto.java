package com.example.MainProject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateProductVariationDto {
    @NotNull(message = "price cant be null")
    @Min(value = 1,message = "price should be atLeast 1")
    private int price;

    @NotNull(message = "quantity cant be null")
    @Min(value = 0,message = " quantity cant be 0")
    private int quantity;

    private boolean active;
}
