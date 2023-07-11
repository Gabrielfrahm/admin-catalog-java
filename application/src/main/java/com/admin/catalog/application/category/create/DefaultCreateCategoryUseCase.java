package com.admin.catalog.application.category.create;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.validation.handler.Notification;
import com.admin.catalog.domain.validation.handler.ThrowsValidationHandler;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends  CreateCategoryUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }


    @Override
    public CreateCategoryOutput execute(final CreateCategoryCommand aCommand) {
        final String aName = aCommand.name();
        final String aDescription = aCommand.description();
        final boolean isActive = aCommand.isActive();

        final var notification = Notification.create();

        final var aCategory = Category.newCategory(aName,aDescription, isActive);

        aCategory.validate(notification);
        if(notification.hasError()){
            //
        }

        return CreateCategoryOutput.from(this.categoryGateway.create(aCategory));
    }
}
