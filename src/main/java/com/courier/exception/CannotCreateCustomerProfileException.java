package com.courier.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CannotCreateCustomerProfileException extends Exception {
    public CannotCreateCustomerProfileException(String message) {
        super(message);
    }
}
