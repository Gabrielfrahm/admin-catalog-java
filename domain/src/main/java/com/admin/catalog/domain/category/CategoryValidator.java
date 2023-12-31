package com.admin.catalog.domain.category;

import com.admin.catalog.domain.validation.Error;
import com.admin.catalog.domain.validation.ValidationHandler;
import com.admin.catalog.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private final Category category;

    public CategoryValidator(final Category aCategory, final ValidationHandler aValidationHandle){
        super(aValidationHandle);
        this.category = aCategory;
    }

    @Override
    public void validate() {
       checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.category.getName();

        if(name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if(name.isBlank()){
            this.validationHandler().append(new Error("'name' should not be empty"));
        }

        final int length = name.trim().length();
        if(length > 255 || length < 3) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }
}
