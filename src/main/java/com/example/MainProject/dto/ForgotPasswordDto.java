package com.example.MainProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ForgotPasswordDto {
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{4,14}$",
            message = "password must contain " +
                    "at" + " least 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
    private String password;
    @NotBlank(message = "password cant be null")
    @NotNull(message = "password should null ")
    private String confirmPassword;
}
