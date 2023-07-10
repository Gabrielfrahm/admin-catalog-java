package com.admin.catalog.domain.category;

import com.admin.catalog.domain.Pagination.Pagination;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {
    Category create(Category aCategory);

    void deleteById(CategoryID anId);

    Optional<Category> findById(CategoryID anId);

    Category update(Category aCategory);

    Pagination<Category> findAll(CategorySearchQuery aQuery);

    List<CategoryID> existsByIds(Iterable<CategoryID> ids);
}
