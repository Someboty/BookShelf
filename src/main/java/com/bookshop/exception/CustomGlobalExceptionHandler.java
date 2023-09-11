package com.bookshop.exception;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ExceptionBody body = new ExceptionBody(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                ex.getBindingResult().getAllErrors().stream()
                        .map(this::getErrorMessage)
                        .toList());
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex) {
        ExceptionBody body = new ExceptionBody(LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                List.of(ex.getMessage()));
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(
            AccessDeniedException ex) {
        ExceptionBody body = new ExceptionBody(LocalDateTime.now(),
                HttpStatus.FORBIDDEN,
                List.of(ex.getMessage()));
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOtherExceptions(Exception ex) {
        ExceptionBody body = new ExceptionBody(LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                List.of(ex.getMessage()));
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getErrorMessage(ObjectError error) {
        if (error instanceof FieldError) {
            return ((FieldError) error).getField() + " " + error.getDefaultMessage();
        }
        return error.getDefaultMessage();
    }
}
