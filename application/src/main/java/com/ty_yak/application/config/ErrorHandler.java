package com.ty_yak.application.config;

import com.ty_yak.auth.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@Slf4j
@RestController
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotValidException.class})
    public final ResponseEntity<ErrorDetails> handleModelNotFoundException(NotValidException ex, WebRequest request) {
        ErrorDetails errorDetails = buildErrorDetails(ex.getMessage(), request.getDescription(false));
        log.error("Bad request", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public final ResponseEntity<ErrorDetails> handleAllExceptions(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = buildErrorDetails(ex.getMessage(), request.getDescription(false));
        log.error("Bad request", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler({AlreadyExistException.class})
    public final ResponseEntity<ErrorDetails> handleAllExceptions(AlreadyExistException ex, WebRequest request) {
        ErrorDetails errorDetails = buildErrorDetails(ex.getMessage(), request.getDescription(false));
        log.error("Bad request", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler({ExpiredException.class})
    public final ResponseEntity<ErrorDetails> handleAllExceptions(ExpiredException ex, WebRequest request) {
        ErrorDetails errorDetails = buildErrorDetails(ex.getMessage(), request.getDescription(false));
        log.error("Bad request", ex);
        return ResponseEntity.status(HttpStatus.GONE).body(errorDetails);
    }

    private static ErrorDetails buildErrorDetails(String message, String details) {
        return new ErrorDetails(new Date(), message, details);
    }
}
