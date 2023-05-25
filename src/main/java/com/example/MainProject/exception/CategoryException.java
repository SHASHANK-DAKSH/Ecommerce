package com.example.MainProject.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CategoryException extends RuntimeException{
    HttpStatus errorCode;

    public CategoryException(String message,HttpStatus errorCode){
        super(message);
        this.errorCode=errorCode;
    }
}
