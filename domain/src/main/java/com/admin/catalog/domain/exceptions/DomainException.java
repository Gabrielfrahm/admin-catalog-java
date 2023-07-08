package com.admin.catalog.domain.exceptions;

import com.admin.catalog.domain.validation.Error;

import java.util.List;

public class DomainException extends RuntimeException {

    protected final List<Error> errors;
    protected DomainException(final List<Error> anErrors){
        super("", null, true, false);
        this.errors = anErrors;
    }

    public static   DomainException with(final List<Error> anErrors) {
        return new DomainException(anErrors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
