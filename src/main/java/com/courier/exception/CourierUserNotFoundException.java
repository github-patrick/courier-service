package com.courier.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CourierUserNotFoundException extends Exception {
    public CourierUserNotFoundException(String message) {
        super(message);
    }
}
