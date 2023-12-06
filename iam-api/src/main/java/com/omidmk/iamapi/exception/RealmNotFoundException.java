package com.omidmk.iamapi.exception;

import org.springframework.http.HttpStatus;

public class RealmNotFoundException extends ApplicationException {
    public RealmNotFoundException() {
        super(HttpStatus.BAD_REQUEST, "realm.not-found");
    }
}