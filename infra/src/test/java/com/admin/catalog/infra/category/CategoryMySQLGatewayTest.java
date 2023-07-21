package com.admin.catalog.infra.category;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.category.CategorySearchQuery;
import com.admin.catalog.MySQLGatewayTest;
import com.admin.catalog.infra.category.persistence.CategoryJpaEntity;
import com.admin.catalog.infra.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAValidCategory_whenCallCreate_shouldReturnANewCategory() {
       final var expectedName = "film";
       final var expectedDescription = "the best category";
       final var expectedIsActive = true;

       final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

       Assertions.assertEquals(0, categoryRepository.count());

       final var actualCategory = categoryMySQLGateway.create(aCategory);

       Assertions.assertEquals(1,categoryRepository.count());

       Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
       Assertions.assertEquals(expectedName, actualCategory.getName());
       Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
       Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
       Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
       Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
       Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
       Assertions.assertNull(aCategory.getDeletedAt());

       final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNull(aCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdate_shouldReturnACategoryUpdate() {
        final var expectedName = "film";
        final var expectedDescription = "the best category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("series", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1,categoryRepository.count());

        final var aUpdateCategory = aCategory.clone().update(expectedName, expectedDescription, expectedIsActive);
        final var actualCategory = categoryMySQLGateway.update(aUpdateCategory);

        Assertions.assertEquals(1,categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNull(aCategory.getDeletedAt());
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        final var aCategory = Category.newCategory("series", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1,categoryRepository.count());

        categoryMySQLGateway.deleteById(aCategory.getId());

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAnInvalidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        Assertions.assertEquals(0, categoryRepository.count());

        categoryMySQLGateway.deleteById(CategoryID.from("invalid ID"));

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenCallsFindById_shouldReturnCategory() {
        final var expectedName = "film";
        final var expectedDescription = "the best category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1,categoryRepository.count());

        final var actualCategory = categoryMySQLGateway.findById(aCategory.getId()).get();
        Assertions.assertEquals(1,categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), (actualCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(aCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategoryIdNoStored_whenCallsFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, categoryRepository.count());
        final var actualCategory = categoryMySQLGateway.findById(CategoryID.from("empty"));
        Assertions.assertTrue(actualCategory.isEmpty());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated(){
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var films = Category.newCategory("films", null, true);
        final var series = Category.newCategory("series", null, true);
        final var doc = Category.newCategory("doc", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(films),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(doc)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0,1, "", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(1, actualResult.items().size());
        Assertions.assertEquals(doc.getId(), actualResult.items().get(0).getId());

    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, categoryRepository.count());
        final var query = new CategorySearchQuery(0,1, "", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedTotal, actualResult.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {

        var expectedPage = 0;
        var expectedPerPage = 1;
        var expectedTotal = 3;

        final var films = Category.newCategory("films", null, true);
        final var series = Category.newCategory("series", null, true);
        final var doc = Category.newCategory("doc", null, true);

        Assertions.assertEquals(0, categoryRepository.count());
        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(films),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(doc)
        ));
        Assertions.assertEquals(3, categoryRepository.count());

        var query = new CategorySearchQuery(0,1, "", "name", "asc");
        var actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(1, actualResult.items().size());
        Assertions.assertEquals(doc.getId(), actualResult.items().get(0).getId());

        //page 1
        expectedPage = 1;
        query = new CategorySearchQuery(1,1, "", "name", "asc");
        actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(1, actualResult.items().size());
        Assertions.assertEquals(films.getId(), actualResult.items().get(0).getId());

        //page 2
        expectedPage = 2;
        query = new CategorySearchQuery(2,1, "", "name", "asc");
        actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(1, actualResult.items().size());
        Assertions.assertEquals(series.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchsCategoryName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var films = Category.newCategory("films", null, true);
        final var series = Category.newCategory("series", null, true);
        final var doc = Category.newCategory("doc", null, true);

        Assertions.assertEquals(0, categoryRepository.count());
        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(films),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(doc)
        ));
        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0,1, "do", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedTotal, actualResult.items().size());
        Assertions.assertEquals(doc.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndTheBestAsTerms_whenCallsFindAllAndTermsMatchsCategoryDescription_shouldReturnPaginated(){
        final var expectedPage = 0;
        final var expectedPerPage = 2;
        final var expectedTotal = 2;

        final var films = Category.newCategory("films", "the best", true);
        final var series = Category.newCategory("series", null, true);
        final var doc = Category.newCategory("doc", "the best", true);

        Assertions.assertEquals(0, categoryRepository.count());
        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(films),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(doc)
        ));
        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0,2, "THE BEST", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedTotal, actualResult.items().size());
    }
}
