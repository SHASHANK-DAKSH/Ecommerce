package com.example.MainProject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenericException extends RuntimeException {
    private HttpStatus errorCode;

    public GenericException(String message,HttpStatus errorCode){
        super(message);
        this.errorCode=errorCode;
    }
}
