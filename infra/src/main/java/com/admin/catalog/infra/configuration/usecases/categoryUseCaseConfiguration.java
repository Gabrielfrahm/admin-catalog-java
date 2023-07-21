package com.admin.catalog.infra.configuration.usecases;

import com.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.admin.catalog.application.category.create.DefaultCreateCategoryUseCase;
import com.admin.catalog.application.category.delete.DefaultDeleteCategoryUseCase;
import com.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.admin.catalog.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalog.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.admin.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.admin.catalog.application.category.update.DefaultUpdateCategoryUseCase;
import com.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.admin.catalog.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class categoryUseCaseConfiguration {
    private final CategoryGateway categoryGateway;

    public categoryUseCaseConfiguration(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase(){
        return new DefaultCreateCategoryUseCase(this.categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase(){
        return new DefaultUpdateCategoryUseCase(this.categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase(){
        return new DefaultDeleteCategoryUseCase(this.categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase(){
        return new DefaultGetCategoryByIdUseCase(this.categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase(){
        return new DefaultListCategoriesUseCase(this.categoryGateway);
    }
}
