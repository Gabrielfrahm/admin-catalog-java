package com.admin.catalog.application.category.create;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.validation.handler.Notification;
import com.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import io.vavr.API;

import static io.vavr.API.Left;
import static io.vavr.API.Try;
import io.vavr.control.Either;
import java.util.Objects;

public class DefaultCreateCategoryUseCase extends  CreateCategoryUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }


    @Override
    public Either<Notification,CreateCategoryOutput>  execute(final CreateCategoryCommand aCommand) {
        final String aName = aCommand.name();
        final String aDescription = aCommand.description();
        final boolean isActive = aCommand.isActive();

        final var notification = Notification.create();

        final var aCategory = Category.newCategory(aName,aDescription, isActive);
        aCategory.validate(notification);

        return notification.hasError() ? Left(notification) : create(aCategory);
     }

     private Either<Notification, CreateCategoryOutput> create(final Category aCategory) {
        return Try(() -> this.categoryGateway.create(aCategory)).toEither().bimap(Notification::create, CreateCategoryOutput::from);
     }
}
