package com.example.MainProject.dto;

import com.example.MainProject.entities.users.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SellerResponseDto {
    private  Long id;
    private String email;
    private String fullName;
    private boolean isActive;
    private String CompanyName;
    private String CompanyContact;
    private Address CompanyAddress;
}
