package com.courier.exception;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;


@Setter
@Getter
@ToString
public class ErrorApi {

    private HttpStatus code;
    private String message;
    private String status;
}
