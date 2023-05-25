package com.example.MainProject.service.authservice;

import com.example.MainProject.dto.CustomerDto;
import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.dto.MessageDto;
import com.example.MainProject.entities.users.Customer;
import com.example.MainProject.entities.users.Role;
import com.example.MainProject.entities.users.Seller;
import com.example.MainProject.entities.users.User;

import com.example.MainProject.exception.CustomerException;
import com.example.MainProject.exception.GenericException;
import com.example.MainProject.others.EmailSender;
import com.example.MainProject.repository.CustomerRepository;
import com.example.MainProject.repository.RoleRepository;
import com.example.MainProject.repository.UserRepository;
import com.example.MainProject.security.JwtGenerator;
import com.example.MainProject.service.authservice.Impl.CustomerServiceImpl;
import jakarta.mail.Multipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

@Service
public class CustomerService implements CustomerServiceImpl {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailSender emailSender;
    @Autowired
    JwtGenerator jwtGenerator;
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    Logger logger= LoggerFactory.getLogger(CustomerService.class);

    @Override
    public MessageDto getCustomerDetails(CustomerDto customerDto) {
        if (userRepository.existsByEmail(customerDto.getEmail())) {
            logger.info("customer already registered");
            throw new CustomerException("email already exist", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(!customerDto.getPassword().equals(customerDto.getConfirmPassword())){
            logger.warn("password not matching");
            throw new CustomerException("password not match",HttpStatus.INTERNAL_SERVER_ERROR);
        }
//        User user=new User();
        Customer customer=new Customer();
        logger.info("calling helper function to data from DTO");
        getCustomerDetailHelp(customer,customerDto);

        userRepository.save(customer);

        String token=jwtGenerator.generateTokenByEmail(customer.getEmail());
        String body="http://localhost:7070/api/auth/customer/register?token="+token;
        emailSender.SimpleEmail(customerDto.getEmail(),"activation token for the user",
                body);
        logger.info("success for adding data");
        return new MessageDto("Customer details added" ,token);

    }
    @Override
    public void getCustomerDetailHelp(Customer customer,CustomerDto customerDto){
        customer.setEmail(customerDto.getEmail());
        customer.setFirstName(customerDto.getFirstName().trim());
        customer.setLastname(customerDto.getLastName().trim());
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customer.setContact(customerDto.getPhoneNumber());

        Role role = roleRepository.findByAuthority("CUSTOMER").orElse(null);
        customer.setRole(Collections.singletonList(role));
    }

    @Override
    public GenerateResponse ActivateToken(String token){

        logger.warn("validating token");
        boolean isValid=jwtGenerator.validateToken(token);
            if(isValid) {

                String username = jwtGenerator.getUserNameFromJwt(token);//gives email
                Customer user = customerRepository.findByEmail(username).orElse(null);

                if (user != null) {

                    user.setActive(true);
                    userRepository.save(user);
                    emailSender.SimpleEmail(username,"ACCOUNT ACTIVATED",
                            "CONGRATULATION YOUR ACCOUNT HAS BEEN CREATED ,PLEASE LOGIN TO USE");
                    return new GenerateResponse(" Account Activated");
                }
                else
                    throw new CustomerException("Token is not of Customer",HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new GenerateResponse("Token Invalid");
    }

    @Override
    public GenerateResponse resendLink(String email){
        logger.warn("checking for existence of email");

        User user= userRepository.findByEmail(email).orElse(null);
        if(user==null){
            throw new GenericException( "user not found please register first" ,HttpStatus.NOT_FOUND);
        }
        if(user.isActive()){ //activate user wants to activate again.
            throw new CustomerException("you have logged in previously,please use Forgot password",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String token=jwtGenerator.generateTokenByEmail(email);
        String body= "http://localhost:7070/api/auth/resend  "+token;
        emailSender.SimpleEmail(email,"Re-Activation Link is",body);
        return new GenerateResponse("reactivation link sent successfully");
    }

}
