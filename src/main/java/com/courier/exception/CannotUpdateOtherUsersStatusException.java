package com.courier.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CannotUpdateOtherUsersStatusException extends Exception {
    public CannotUpdateOtherUsersStatusException(String message) {
        super(message);
    }
}
