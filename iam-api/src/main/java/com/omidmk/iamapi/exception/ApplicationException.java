package com.omidmk.iamapi.exception;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApplicationException extends Exception {
    private final HttpStatusCode statusCode;

    @Nullable
    private final String reason;

    public ApplicationException(HttpStatusCode statusCode) {
        this(statusCode, null);
    }

    public ApplicationException(HttpStatusCode statusCode, @Nullable String reason) {
        this.statusCode = statusCode;
        this.reason = reason;
    }
}
