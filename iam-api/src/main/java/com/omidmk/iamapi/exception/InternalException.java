package com.omidmk.iamapi.exception;

import org.springframework.http.HttpStatus;

public class InternalException extends ApplicationException {

    public InternalException(String reason) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, reason);
    }
}