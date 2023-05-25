package com.example.MainProject.exception;

import com.example.MainProject.dto.GenerateResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails>handleAllException(Exception e,WebRequest request){
        ErrorDetails errorDetails=new ErrorDetails(new Date(),
                e.getMessage(), request.getDescription(false));

        return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<?>handleAuthenticationCredentialsNotFoundException
            (AuthenticationCredentialsNotFoundException e){
        GenerateResponse message=new GenerateResponse();
        message.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(message);
    }
    @ExceptionHandler(GenericException.class)
    public ResponseEntity<?>handleGeneric(GenericException e,WebRequest request){
        GenerateResponse response=new GenerateResponse();
        String message=e.getMessage();
        response.setMessage(message);
        //response.setLocalDateTime(LocalDateTime.now());
        HttpStatus status=e.getErrorCode();
        return new ResponseEntity<>(response,status);
    }
    @ExceptionHandler(AdminException.class)
    public ResponseEntity<?>handleGeneric2(AdminException e,WebRequest request){
        GenerateResponse response=new GenerateResponse();
        String message=e.getMessage();
        response.setMessage(message);
        //response.setLocalDateTime(LocalDateTime.now());
        HttpStatus status=e.getCode();
        return new ResponseEntity<>(response,status);
    }
    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<?>handleGeneric3(CategoryException e,WebRequest request){
        GenerateResponse response=new GenerateResponse();
        String message=e.getMessage();
        response.setMessage(message);
        //response.setLocalDateTime(LocalDateTime.now());
        HttpStatus status=e.getErrorCode();
        return new ResponseEntity<>(response,status);
    }
    @ExceptionHandler(CustomerException.class)
    public ResponseEntity<?>handleGeneric4(CustomerException e,WebRequest request){
        GenerateResponse response=new GenerateResponse();
        String message=e.getMessage();
        response.setMessage(message);
        //response.setLocalDateTime(LocalDateTime.now());
        HttpStatus status=e.getErrorCode();
        return new ResponseEntity<>(response,status);
    }
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<?>handleGeneric5(ProductException e,WebRequest request){
        GenerateResponse response=new GenerateResponse();
        String message=e.getMessage();
        response.setMessage(message);
        //response.setLocalDateTime(LocalDateTime.now());
        HttpStatus status=e.getErrorCode();
        return new ResponseEntity<>(response,status);
    }
    @ExceptionHandler(SellerException.class)
    public ResponseEntity<?>handleGeneric6(SellerException e,WebRequest request){
        GenerateResponse response=new GenerateResponse();
        String message=e.getMessage();
        response.setMessage(message);
        //response.setLocalDateTime(LocalDateTime.now());
        HttpStatus status=e.getErrorCode();
        return new ResponseEntity<>(response,status);
    }
}
