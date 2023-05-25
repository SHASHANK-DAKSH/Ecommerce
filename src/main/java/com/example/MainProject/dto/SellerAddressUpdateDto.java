package com.example.MainProject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SellerAddressUpdateDto {


    // not applying validation here so that user can update according to his need
//    @NotNull(message = "city Cant be null ")
//    @NotBlank(message = "city Cant be blank ")
    private String city;

//    @NotNull(message = "state Cant be null ")
//    @NotBlank(message = "state Cant be blank ")
    private String state;

    private String country;

    private String addressLine;

//    @Pattern(regexp = "^[0-9]{6}(?:-[0-9]{5})?$",message = "give right format of zipcode")
    private  int zipCode;

    private String Label;
}
