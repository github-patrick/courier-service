package com.courier.controller;

import com.courier.exception.CourierUserNotFoundException;
import com.courier.exception.ErrorApi;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorApi handleValidationException(MethodArgumentNotValidException ex) {

        ErrorApi errorApi = new ErrorApi();
        errorApi.setMessage(ex.getMessage());
        errorApi.setCode(HttpStatus.BAD_REQUEST);
        return errorApi;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CourierUserNotFoundException.class)
    public ErrorApi handleCourierUserNotFoundException(CourierUserNotFoundException ex) {
        ErrorApi errorApi = new ErrorApi();
        errorApi.setMessage(ex.getMessage());
        errorApi.setCode(HttpStatus.BAD_REQUEST);
        return errorApi;
    }
}
