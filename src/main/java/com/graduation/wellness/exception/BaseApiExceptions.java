package com.graduation.wellness.exception;

import org.springframework.http.HttpStatus;

public class BaseApiExceptions extends RuntimeException {
    private final HttpStatus httpStatus;

    public BaseApiExceptions(String message , HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus ;
    }
    public HttpStatus getStatusCode(){
        return httpStatus;
    }

}
