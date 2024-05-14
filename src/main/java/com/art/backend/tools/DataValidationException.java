package com.art.backend.tools;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class DataValidationException extends Exception{
    public DataValidationException(String message){
        super(message);
    }
}
