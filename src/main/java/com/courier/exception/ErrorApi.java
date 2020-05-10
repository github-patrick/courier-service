package com.courier.exception;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Setter
@Getter
@ToString
public class ErrorApi {

    private LocalDateTime timestamp = LocalDateTime.now();
    private String code;
    private String message;
    private String status;
}
