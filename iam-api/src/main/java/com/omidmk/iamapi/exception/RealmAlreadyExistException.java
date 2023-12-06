package com.omidmk.iamapi.exception;

import org.springframework.http.HttpStatus;

public class RealmAlreadyExistException extends ApplicationException {
    public RealmAlreadyExistException() {
        super(HttpStatus.BAD_REQUEST, "realm.already-exists");
    }
}