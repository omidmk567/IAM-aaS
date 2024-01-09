package com.omidmk.iamapi.exception;

import org.springframework.http.HttpStatus;

public class DeploymentNotFoundException extends ApplicationException {

    public DeploymentNotFoundException() {
        super(HttpStatus.NOT_FOUND, "deployment.not-found");
    }
}