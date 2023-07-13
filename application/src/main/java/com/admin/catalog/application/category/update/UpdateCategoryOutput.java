package com.admin.catalog.application.category.update;

import com.admin.catalog.domain.category.Category;

public record UpdateCategoryOutput(
        Category id
) {

    public static UpdateCategoryOutput from(final Category category){
        return new UpdateCategoryOutput(category);
    }
}
