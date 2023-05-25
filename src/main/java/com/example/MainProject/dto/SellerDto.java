package com.example.MainProject.dto;

import com.example.MainProject.entities.users.Address;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SellerDto {
    @NotNull
    @NotBlank
    @Email(message = "Email should be in right format")
    private String email;


    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{4,14}$",
            message = "password must contain " +
            "at" + " least 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
    private String password;


    @NotBlank(message = "password cant be null")
    @NotNull(message = "password should match ")
    private String confirmPassword;


    @NotNull
    @Pattern(regexp="^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",message = "GST  format " +
            "not accurate")
    private String gst;


    @NotNull(message = "company name should have some name ")
    @NotBlank(message = "company name cant be blank")
    private String companyName;


    @NotNull(message = "add your address")
    private Address companyAddress;


    @NotNull
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$",
            message = "must have 10 numbers")
    private String companyContact;


    @NotNull(message = "cant be null")
    @NotBlank(message = "firstName cant be blank")
    private String firstName;


    private String lastName;


}
