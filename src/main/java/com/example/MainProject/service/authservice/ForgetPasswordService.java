package com.example.MainProject.service.authservice;


import com.example.MainProject.dto.ForgotPasswordDto;
import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.entities.users.User;
import com.example.MainProject.exception.GenericException;
import com.example.MainProject.others.EmailSender;
import com.example.MainProject.repository.TokenRepo;
import com.example.MainProject.repository.UserRepository;
import com.example.MainProject.security.JwtGenerator;
import com.example.MainProject.service.authservice.Impl.ForgotPasswordImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ForgetPasswordService implements ForgotPasswordImpl {
    @Autowired
    TokenRepo tokenRepo;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtGenerator jwtGenerator;

    @Autowired
    EmailSender emailSender;

    @Autowired
    PasswordEncoder passwordEncoder;

    Logger logger= LoggerFactory.getLogger(ForgetPasswordService.class);

    @Override
    public GenerateResponse forgetPassword(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        if(Objects.isNull(user)){
            logger.info("user not found");
            throw  new GenericException("no Account Found",HttpStatus.NOT_FOUND);
        }

        if(!user.isActive()){
            logger.info("user not active");
            throw  new GenericException("No Active Account,go to resend Activation link",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(tokenRepo.existsByEmail(user.getEmail())) {//if user hit more than 1 time on forgot.
            tokenRepo.deleteByEmail(user.getEmail());
        }
        user.setActive(false); //ask whether to include or not,since you will only get one token if included
        logger.info("user token deleted");
        userRepository.save(user);
        String token = jwtGenerator.generateTokenByEmail(email);

        emailSender.SimpleEmail(email,"new token",token);
        GenerateResponse gg = new GenerateResponse("");
        gg.setMessage("new token send");
        logger.info("response sent giving back to controller");
        return gg;


    }


    @Override
    public GenerateResponse saveNewPassword(String token,ForgotPasswordDto forgotPasswordDto) {

        logger.warn("validating token");
        if (jwtGenerator.validateToken(token)) {
            String username = jwtGenerator.getUserNameFromJwt(token);
            User user = userRepository.findByEmail(username).orElse(null);

            if (user == null) {
                logger.info("user is null");
                GenerateResponse gg = new GenerateResponse();
                throw new GenericException("User not found", HttpStatus.NOT_FOUND);
            }

            if(!forgotPasswordDto.getPassword().equals(forgotPasswordDto.getConfirmPassword())){
                throw new GenericException("Password and confirm password doesn't match",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            user.setPassword(passwordEncoder.encode(forgotPasswordDto.getPassword()));

            user.setActive(true);
            userRepository.save(user);


            logger.info("password updation success");
            emailSender.SimpleEmail(user.getEmail(), "Password Updated", "you have updated your password");

            return new GenerateResponse("Password Updated");
        }
        return new GenerateResponse("Token Invalid");

    }
}
