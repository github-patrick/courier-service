package com.courier.controller;

import com.courier.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class HandleExceptionControllerAdvice extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ErrorApi errorApi = new ErrorApi(status.getReasonPhrase(),ex.getMessage(), details,status.value());
        return new ResponseEntity(errorApi, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        ErrorApi errorApi = new ErrorApi(status.getReasonPhrase(),ex.getMessage(), details,status.value());
        return new ResponseEntity(errorApi, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CourierUserNotFoundException.class)
    public ResponseEntity<Object> handleCourierUserNotFoundException(CourierUserNotFoundException ex, WebRequest request) {

        ErrorApi errorApi = new ErrorApi(HttpStatus.BAD_REQUEST.getReasonPhrase(),ex.getMessage(), null,
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity(errorApi, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CannotRegisterUserException.class)
    protected ResponseEntity<Object> handleCannotRegisterUserException(CannotRegisterUserException ex, WebRequest request) {
        ErrorApi errorApi = new ErrorApi(HttpStatus.BAD_REQUEST.getReasonPhrase(),ex.getMessage(), null,
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity(errorApi, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CannotCreateCustomerProfileException.class)
    protected ResponseEntity<Object> handleCannotCreateCustomerProfileException(CannotCreateCustomerProfileException ex,
                                                                                WebRequest request) {
        ErrorApi errorApi = new ErrorApi(HttpStatus.BAD_REQUEST.getReasonPhrase(),ex.getMessage(), null,
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity(errorApi, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CannotCreateDriverProfileException.class)
    protected ResponseEntity<Object> handleCannotCreateDriverProfileException(CannotCreateDriverProfileException ex,
                                                                                WebRequest request) {
        ErrorApi errorApi = new ErrorApi(HttpStatus.BAD_REQUEST.getReasonPhrase(),ex.getMessage(), null,
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity(errorApi, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DeliveryDriverNotFoundException.class)
    public ResponseEntity<Object> handleDeliveryDriverNotFoundException(DeliveryDriverNotFoundException ex, WebRequest request) {
        ErrorApi errorApi = new ErrorApi(HttpStatus.NOT_FOUND.getReasonPhrase(),ex.getMessage(), null,
                HttpStatus.NOT_FOUND.value());
        return new ResponseEntity(errorApi, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CannotUpdateOtherUsersStatusException.class)
    public ResponseEntity<Object> handleCannotUpdateOtherUsersStatusException(CannotUpdateOtherUsersStatusException ex, WebRequest request) {
        ErrorApi errorApi = new ErrorApi(HttpStatus.FORBIDDEN.getReasonPhrase(),ex.getMessage(), null,
                HttpStatus.FORBIDDEN.value());
        return new ResponseEntity(errorApi, HttpStatus.FORBIDDEN);
    }
}
