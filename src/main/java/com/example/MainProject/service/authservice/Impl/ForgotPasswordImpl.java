package com.example.MainProject.service.authservice.Impl;

import com.example.MainProject.dto.ForgotPasswordDto;
import com.example.MainProject.dto.GenerateResponse;

public interface ForgotPasswordImpl {
    public GenerateResponse forgetPassword(String email);

    public GenerateResponse saveNewPassword(String token, ForgotPasswordDto forgotPasswordDto);
}
