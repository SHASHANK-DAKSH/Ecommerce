package com.example.MainProject.Controllers.authcontroller;


import com.example.MainProject.MainProjectApplication;
import com.example.MainProject.dto.ForgotPasswordDto;
import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.dto.LoginDto;

import com.example.MainProject.dto.MessageDto;
import com.example.MainProject.service.authservice.ForgetPasswordService;
import com.example.MainProject.service.authservice.Impl.LoginServiceImpl;
import com.example.MainProject.service.authservice.Impl.LogoutServiceImpl;
import com.example.MainProject.service.authservice.LoginService;
import com.example.MainProject.service.authservice.LogoutService;
import jakarta.validation.Valid;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Data

public class LogControllers {


    Logger logger= LoggerFactory.getLogger(LogControllers.class);

    @Autowired
    LoginServiceImpl logService;

    @Autowired
    LogoutServiceImpl logoutService;

    @Autowired
    ForgetPasswordService forgetPasswordService;




    @PostMapping("/login")
    public ResponseEntity<MessageDto>login(@Valid @RequestBody LoginDto loginDto){
        logger.info("service called for seller or customer login");
      MessageDto message=logService.loginCustomerDb(loginDto);
      return new ResponseEntity<>(message,HttpStatus.CREATED);
    }


    @PostMapping("/logout")
    public ResponseEntity<GenerateResponse>logout(@RequestHeader String token){
        logger.info("service called for seller or customer logout");
            GenerateResponse message= logoutService.logoutCustomer(token);
            return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @PostMapping("/forgot")
    public ResponseEntity<GenerateResponse> forgotPassword(@RequestHeader String email){
        logger.info("service called for forget password");
        GenerateResponse message=forgetPasswordService.forgetPassword(email);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<GenerateResponse>updatePassword(@RequestHeader String token,
                                                          @Valid @RequestBody ForgotPasswordDto forgotPasswordDto){
        logger.info("service called for reset password");
        GenerateResponse message=forgetPasswordService.saveNewPassword(token,forgotPasswordDto);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }


}













































//   @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
//        if (userRepository.existsByEmail(registerDto.getEmail())) {
//            return new ResponseEntity<>("email already taken", HttpStatus.BAD_REQUEST);
//        }
//        User user = new User();
//        user.setEmail(registerDto.getEmail());
//        user.setFirstName(registerDto.getFirstName());
//        user.setLastname(registerDto.getLastName());
//        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
//
//        Role role = roleRepository.findByAuthority("CUSTOMER").get();
//        user.setRole(Collections.singletonList(role));
//
//        userRepository.save(user);
//
//        return new ResponseEntity<>("user details added", HttpStatus.OK);
//    }
