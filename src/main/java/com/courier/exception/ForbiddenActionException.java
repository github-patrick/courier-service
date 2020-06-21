package com.courier.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ForbiddenActionException extends Exception {
    public ForbiddenActionException(String message) {
        super(message);
    }
}
