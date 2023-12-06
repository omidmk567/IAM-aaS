package com.omidmk.iamapi.exception;

import org.springframework.http.HttpStatus;

public class UserCreationException extends ApplicationException {
    public UserCreationException() {
        super(HttpStatus.BAD_REQUEST, "user.not-created");
    }
}