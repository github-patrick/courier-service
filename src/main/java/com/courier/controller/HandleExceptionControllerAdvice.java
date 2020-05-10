package com.courier.controller;

import com.courier.exception.CannotCreateCustomerProfileException;
import com.courier.exception.CannotRegisterUserException;
import com.courier.exception.CourierUserNotFoundException;
import com.courier.exception.ErrorApi;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class HandleExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorApi handleValidationException(MethodArgumentNotValidException ex) {

        ErrorApi errorApi = new ErrorApi();
        errorApi.setMessage(ex.getMessage());
        errorApi.setCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return errorApi;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CourierUserNotFoundException.class)
    public ErrorApi handleCourierUserNotFoundException(CourierUserNotFoundException ex) {
        ErrorApi errorApi = new ErrorApi();
        errorApi.setMessage(ex.getMessage());
        errorApi.setCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return errorApi;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CannotRegisterUserException.class)
    public ErrorApi handleCannotRegisterUserException(CannotRegisterUserException ex) {
        ErrorApi errorApi = new ErrorApi();
        errorApi.setMessage(ex.getMessage());
        errorApi.setCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return errorApi;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CannotCreateCustomerProfileException.class)
    public ErrorApi handleCannotCreateCustomerProfileException(CannotCreateCustomerProfileException ex) {
        ErrorApi errorApi = new ErrorApi();
        errorApi.setMessage(ex.getMessage());
        errorApi.setCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return errorApi;

    }
}
