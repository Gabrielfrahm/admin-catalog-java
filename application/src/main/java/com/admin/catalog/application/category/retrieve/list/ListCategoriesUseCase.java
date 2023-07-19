package com.admin.catalog.application.category.retrieve.list;

import com.admin.catalog.application.UseCase;
import com.admin.catalog.domain.Pagination.Pagination;
import com.admin.catalog.domain.category.CategorySearchQuery;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
