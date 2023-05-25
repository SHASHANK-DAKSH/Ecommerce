package com.example.MainProject.service.authservice.Impl;

import com.example.MainProject.dto.CustomerDto;
import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.dto.MessageDto;
import com.example.MainProject.entities.users.Customer;

public interface CustomerServiceImpl {

    public MessageDto getCustomerDetails(CustomerDto customerDto);

    public void getCustomerDetailHelp(Customer customer, CustomerDto customerDto);

    public GenerateResponse ActivateToken(String token);

    public GenerateResponse resendLink(String email);
}
