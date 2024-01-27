package com.omidmk.iamapi.exception;

import org.springframework.http.HttpStatus;

public class ClosedTicketModifyingException extends ApplicationException {

    public ClosedTicketModifyingException() {
        super(HttpStatus.BAD_REQUEST, "ticket.closed");
    }
}