package com.ty_yak.auth.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ResponseStatus(value = UNAUTHORIZED)
public class JwtException extends RuntimeException {

    public JwtException(String message) {
        super(message);
    }
}
