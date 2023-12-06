package com.omidmk.iamapi.exception;

import org.springframework.http.HttpStatus;

public class PlanNotFoundException extends ApplicationException {
    public PlanNotFoundException() {
        super(HttpStatus.BAD_REQUEST, "plan.not-found");
    }
}