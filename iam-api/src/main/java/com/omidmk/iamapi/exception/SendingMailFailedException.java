package com.omidmk.iamapi.exception;

import org.springframework.http.HttpStatus;

public class SendingMailFailedException extends ApplicationException {
    public SendingMailFailedException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "send-mail.failed");
    }

    public SendingMailFailedException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "send-mail.failed", message);
    }
}