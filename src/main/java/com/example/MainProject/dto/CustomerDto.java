package com.example.MainProject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerDto {
    @Email(message = "write email in correct form")
    @NotBlank(message = "cant be blank")
    private String email;


    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{4,14}$",
            message = "password must contain " +
            "at" + " least 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
    private String password;


    @NotBlank(message = "password cant be null")
    @NotNull(message = "password should null ")
    private String confirmPassword;

    @NotNull(message = "firstName cant be null")
    @NotBlank(message = "firstName should be something")
    private String firstName;

    @NotNull(message = "LastName cant be null,add (.) if not having last name ")
    @NotBlank(message = "firstName should be something ,add (.) if not having last name ")
    private String lastName;

    @NotNull
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$",
            message = "must have 10 numbers")
    private String phoneNumber;
}
