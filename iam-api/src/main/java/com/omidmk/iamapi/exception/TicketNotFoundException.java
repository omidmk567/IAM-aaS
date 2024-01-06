package com.omidmk.iamapi.exception;

import org.springframework.http.HttpStatus;

public class TicketNotFoundException extends ApplicationException {

    public TicketNotFoundException() {
        super(HttpStatus.NOT_FOUND, "ticket.not-found");
    }
}