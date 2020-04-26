package com.courier.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CannotCreateDriverProfileException extends Exception {

    public CannotCreateDriverProfileException(String message) {
        super(message);
    }
}
