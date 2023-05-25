package com.example.MainProject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductUpdateDto {
    @NotNull(message = "name cant be null")
    @NotBlank(message = "name cant be blank")
    private String name;
    private String description;
    private boolean cancelable;
    private boolean returnable;
}
