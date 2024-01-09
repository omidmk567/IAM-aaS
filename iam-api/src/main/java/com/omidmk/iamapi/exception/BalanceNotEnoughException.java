package com.omidmk.iamapi.exception;

import org.springframework.http.HttpStatus;

public class BalanceNotEnoughException extends ApplicationException {

    public BalanceNotEnoughException() {
        super(HttpStatus.BAD_REQUEST, "balance.not-enough");
    }
}