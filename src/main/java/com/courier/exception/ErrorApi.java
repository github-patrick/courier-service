package com.courier.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;


@Setter
@Getter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorApi {

    private LocalDateTime timestamp = LocalDateTime.now();
    private String code;
    private String message;
    private List<String> details;
    private int status;


    public ErrorApi(String code, String message, List<String> details, int status) {
        this.code = code;
        this.message = message;
        this.details = details;
        this.status = status;
    }
}
