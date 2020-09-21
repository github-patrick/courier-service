package com.courier.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CannotCreateParcelException extends Exception {
    public CannotCreateParcelException(String message) {
        super(message);
    }
}
