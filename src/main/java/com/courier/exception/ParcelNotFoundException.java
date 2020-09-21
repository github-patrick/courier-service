package com.courier.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ParcelNotFoundException extends Exception {
    public ParcelNotFoundException(String message) {
        super(message);
    }
}
