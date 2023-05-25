package com.example.MainProject.security;

import com.example.MainProject.dto.GenerateResponse;
import com.example.MainProject.entities.token.Tokens;
import com.example.MainProject.repository.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
public class TokenValidation {

    @Autowired
    TokenRepo tokenRepo;


    public boolean validToken(String token){
        Tokens tokens=tokenRepo.findByToken(token).orElse(null);

        if(Objects.isNull(tokens)||tokens.getExpiration().before(new Date())){
            return false;
        }
        return true;
    }

    public ResponseEntity<?>inValidToken(String token){
        Tokens tokens=tokenRepo.findByToken(token).orElse(null);
        if(Objects.isNull(tokens)){
            return new ResponseEntity<>(new GenerateResponse("Invalid Token"), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(new GenerateResponse("Your session is Expired"),HttpStatus.UNAUTHORIZED);
    }
}
