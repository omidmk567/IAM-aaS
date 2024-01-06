package com.omidmk.iamapi.exception;

import org.springframework.http.HttpStatus;

public class DialogNotFoundException extends ApplicationException {

    public DialogNotFoundException() {
        super(HttpStatus.NOT_FOUND, "dialog.not-found");
    }
}