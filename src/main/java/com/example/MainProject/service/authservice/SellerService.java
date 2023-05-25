package com.example.MainProject.service.authservice;

import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.dto.SellerDto;
import com.example.MainProject.entities.users.Role;
import com.example.MainProject.entities.users.Seller;

import com.example.MainProject.exception.SellerException;
import com.example.MainProject.others.EmailSender;
import com.example.MainProject.repository.RoleRepository;
import com.example.MainProject.repository.SellerRepository;
import com.example.MainProject.repository.UserRepository;
import com.example.MainProject.security.JwtGenerator;
import com.example.MainProject.service.authservice.Impl.SellerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SellerService implements SellerServiceImpl {

    @Autowired
    UserRepository userRepository;


    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    JwtGenerator jwtGenerator;

    @Autowired
    EmailSender emailSender;


    Logger logger= LoggerFactory.getLogger(SellerService.class);

    @Override
    public GenerateResponse getSellerDetails(SellerDto sellerDto) {
        logger.warn("checking for email existence");
        if (userRepository.existsByEmail(sellerDto.getEmail())) {
            throw new SellerException("email already taken", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("checking for password");
        if(!sellerDto.getPassword().equals(sellerDto.getConfirmPassword())){
            throw new SellerException("password not match",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(sellerRepository.existsByCompanyNameIgnoreCase(sellerDto.getCompanyName())){
            throw new SellerException("Company name not unique",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(sellerRepository.existsByGstNumberIgnoreCase(sellerDto.getGst())){
            throw new SellerException("Gst number is of other seller enter new",HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Seller seller = new Seller();
        getSellerDetailsHelp(seller,sellerDto);

        emailSender.SimpleEmail(sellerDto.getEmail(),"activation  for the seller ",
                "waiting for your account to get approved by admin");
        userRepository.save(seller);
         logger.info("Seller details added");
        return new GenerateResponse("Seller details added");

    }
    public void getSellerDetailsHelp(Seller seller,SellerDto sellerDto){
        seller.setPassword(passwordEncoder.encode(sellerDto.getPassword()));
        seller.setFirstName(sellerDto.getFirstName().trim());
        seller.setLastname(sellerDto.getLastName().trim());
        seller.setEmail(sellerDto.getEmail());
        seller.setGstNumber(sellerDto.getGst());
        seller.setCompanyContact(sellerDto.getCompanyContact());
        seller.setCompanyName(sellerDto.getCompanyName().trim());
        seller.setAddress(sellerDto.getCompanyAddress());
        sellerDto.getCompanyAddress().setSeller(seller);// for adding id to address.

        Role role = roleRepository.findByAuthority("SELLER").get();
        seller.setRole(Collections.singletonList(role));
    }


}
