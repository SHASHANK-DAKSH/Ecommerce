package com.example.MainProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetCustomerResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private String Contact;
    private String image;
    // image not including now.
}
