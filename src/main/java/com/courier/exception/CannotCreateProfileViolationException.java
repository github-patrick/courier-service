package com.courier.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CannotCreateProfileViolationException extends Exception {
    public CannotCreateProfileViolationException(String message) {
        super(message);
    }
}
