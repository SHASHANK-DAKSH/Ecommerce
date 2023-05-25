package com.example.MainProject.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductException extends RuntimeException{
    HttpStatus errorCode;
    public ProductException(String message,HttpStatus errorCode){
        super(message);
        this.errorCode=errorCode;
    }
}
