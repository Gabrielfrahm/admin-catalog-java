package com.admin.catalog.application.category.retrieve.list;

import com.admin.catalog.domain.Pagination.Pagination;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategorySearchQuery;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {

    private CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(final CategorySearchQuery aQuery) {
       return this.categoryGateway.findAll(aQuery).map(CategoryListOutput::from);
    }
}
