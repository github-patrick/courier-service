package com.courier.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DeliveryDriverNotFoundException extends Exception {
    public DeliveryDriverNotFoundException(String message) {
        super(message);
    }
}
