package com.ty_yak.auth.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

@ResponseStatus(value = BAD_REQUEST)
public class NotValidException extends RuntimeException {

    public NotValidException(String message) {
        super(message);
    }
}
