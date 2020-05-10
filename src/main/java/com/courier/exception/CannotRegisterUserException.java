package com.courier.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CannotRegisterUserException extends Exception {
    public CannotRegisterUserException(String message) {
        super(message);
    }
}
