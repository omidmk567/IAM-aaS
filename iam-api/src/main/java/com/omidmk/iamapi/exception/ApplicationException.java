package com.omidmk.iamapi.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.Nullable;

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
        this(statusCode, reason, String.valueOf(reason));
    }

    public ApplicationException(HttpStatusCode statusCode, @Nullable String reason, String message) {
        super(message);
        this.statusCode = statusCode;
        this.reason = reason;
    }
}
