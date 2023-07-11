package com.admin.catalog.application.category.create;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.category.CategorySearchQuery;

public record CreateCategoryOutput(
        CategoryID id
) {

    public static CreateCategoryOutput from(final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId());
    }
}
