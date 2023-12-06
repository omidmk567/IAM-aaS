package com.omidmk.iamapi.controller.exception;

import com.omidmk.iamapi.exception.ApplicationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Map<String, Object>> handleCustomNotFoundException(ApplicationException ex) {
        var body = new HashMap<String, Object>();
        body.put("status", ex.getStatusCode().value());
        body.put("reason", ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }
}
