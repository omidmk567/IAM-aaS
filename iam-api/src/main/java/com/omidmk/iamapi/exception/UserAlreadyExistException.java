package com.omidmk.iamapi.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends ApplicationException {

    public UserAlreadyExistException() {
        super(HttpStatus.BAD_REQUEST, "user.already-exists");
    }
}