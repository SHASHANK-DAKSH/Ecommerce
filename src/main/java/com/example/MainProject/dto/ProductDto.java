package com.example.MainProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDto {
    @NotNull(message = "cant be null")
    private Long id;//category id
    @NotBlank(message = "name of product cant be blank")
    private String name;
    private String brand;
    private String description;
    private boolean cancelable;
    private boolean returnable;
}
