package com.omidmk.iamapi.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApplicationException {

    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "user.not-found");
    }
}