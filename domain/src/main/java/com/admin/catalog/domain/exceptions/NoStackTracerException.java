package com.admin.catalog.domain.exceptions;

public class NoStackTracerException extends  RuntimeException{
    public NoStackTracerException(final String message) {
        this(message, null);
    }

    public NoStackTracerException(final String message, final Throwable cause) {
        super(message, cause, true, false);
    }
}
