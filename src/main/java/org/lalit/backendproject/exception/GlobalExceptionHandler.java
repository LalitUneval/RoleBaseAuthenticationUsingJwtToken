package org.lalit.backendproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice// use to handle the Exception Globley
public class GlobalExceptionHandler {

    @ExceptionHandler(UserEnableToRegister.class)
    public ResponseEntity<String> handleUnableToRegister(UserEnableToRegister ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserNotValid.class)
    public ResponseEntity<String> handleUserValidare(UserNotValid ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
