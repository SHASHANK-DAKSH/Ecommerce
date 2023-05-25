package com.example.MainProject.service.authservice.Impl;

import com.example.MainProject.dto.LoginDto;
import com.example.MainProject.dto.MessageDto;

public interface LoginServiceImpl {

    public MessageDto loginCustomerDb(LoginDto loginDto);
}
