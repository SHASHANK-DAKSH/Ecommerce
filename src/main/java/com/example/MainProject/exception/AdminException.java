package com.example.MainProject.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AdminException extends RuntimeException{
    private HttpStatus code;


    public AdminException(String message,HttpStatus code){
        super(message);
        this.code=code;
    }
}
