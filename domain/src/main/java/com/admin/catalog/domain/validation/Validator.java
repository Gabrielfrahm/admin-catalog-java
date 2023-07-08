package com.admin.catalog.domain.validation;

public abstract class Validator {
    private final ValidationHandler hanlder;

    protected  Validator(final ValidationHandler anHandler){
        this.hanlder = anHandler;
    }

    public abstract  void validate();

    protected ValidationHandler validationHandler() {
        return this.hanlder;
    }
}
