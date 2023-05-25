package com.example.MainProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressDto {
    @NotNull(message = "country Cant be null ")
    @NotBlank(message = "country Cant be blank ")
    private String country;
    @NotNull(message = "state Cant be null ")
    @NotBlank(message = "State Cant be blank")
    private String state;
    private String label;

    @NotNull(message = "zipCode Cant be null ")
   // @Pattern(regexp = "^\\d{5}(?:[-\\s]\\d{4})?$",message = "give right format of zipcode")
    private Integer zipCode;
    private String city;
    @NotNull(message = "AddressLine Cant be null ")
    @NotBlank(message = "AddressLine Cant be blank")
    private String AddressLine;

}
