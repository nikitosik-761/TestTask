package com.testApp.testApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotSuitableAuthority extends RuntimeException{
    public NotSuitableAuthority(String message) {
        super(message);
    }
}
