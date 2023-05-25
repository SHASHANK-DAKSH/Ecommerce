package com.example.MainProject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {

    @NotNull
    @NotBlank
    @Email(message = "Email should be in right format")
    private String email;
    
    private String password;

}
