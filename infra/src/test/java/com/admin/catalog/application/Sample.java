package com.admin.catalog.application;

import com.admin.catalog.IntegrationTest;
import com.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.admin.catalog.infra.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class Sample {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInjects(){
        Assertions.assertNotNull(useCase);
        Assertions.assertNotNull(categoryRepository);
    }
}
