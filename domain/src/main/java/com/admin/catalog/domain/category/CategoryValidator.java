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
        if(this.category.getName() == null){
            this.validationHandler().append(new Error("'name' should not be null"));
        }
    }
}
