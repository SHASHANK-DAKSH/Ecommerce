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
public class SellerDataListDto {
    private Long id;
    private String firstName;
    private String LastName;
    private boolean isActive;
    private String CompanyContact;
    private String gst;
    private String image;//will configure later
    private Address address;

}
