package com.admin.catalog.application.category.update;

import com.admin.catalog.application.category.create.CreateCategoryCommand;
import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {
    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }


    @Test
    public void giveAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId(){
        final var aCategory = Category.newCategory("film", null, true);

        final var expectedName = "film updated";
        final var expectedDescription = "description updated";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(Category.with(aCategory)));
        Mockito.when(categoryGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

        final var actualOutPut = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutPut);
        Assertions.assertNotNull(actualOutPut.id());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(categoryGateway, times(1)).update(argThat(aUpdateCategory ->
                Objects.equals(expectedName, aUpdateCategory.getName())
                        && Objects.equals(expectedDescription, aUpdateCategory.getDescription())
                        && Objects.equals(expectedIsActive, aUpdateCategory.isActive())
                        && Objects.equals(expectedId, aUpdateCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && aCategory.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt())
                        && Objects.isNull(aCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var aCategory = Category.newCategory("film", null, true);
        final String expectedName = null;
        final var expectedDescription = "this category is most watch";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(Category.with(aCategory)));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, times(0)).update(any());

    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsUpdateCategory_shouldReturnInactiveCategoryId(){
        final var aCategory = Category.newCategory("film", null, true);

        final var expectedName = "film updated";
        final var expectedDescription = "description updated";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(Category.with(aCategory)));
        Mockito.when(categoryGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutPut = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutPut);
        Assertions.assertNotNull(actualOutPut.id());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        Mockito.verify(categoryGateway, times(1)).update(argThat(aUpdateCategory ->
                Objects.equals(expectedName, aUpdateCategory.getName())
                        && Objects.equals(expectedDescription, aUpdateCategory.getDescription())
                        && Objects.equals(expectedIsActive, aUpdateCategory.isActive())
                        && Objects.equals(expectedId, aUpdateCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && aCategory.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt())
                        && Objects.nonNull(aUpdateCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException(){
        final var aCategory = Category.newCategory("film", null, true);

        final var expectedName = "film";
        final var expectedDescription = "this category is most watch";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue() ,expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(Category.with(aCategory)));
        when(categoryGateway.update(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, times(1)).update(argThat(aUpdateCategory ->
                Objects.equals(expectedName, aUpdateCategory.getName())
                        && Objects.equals(expectedDescription, aUpdateCategory.getDescription())
                        && Objects.equals(expectedIsActive, aUpdateCategory.isActive())
                        && Objects.equals(expectedId, aUpdateCategory.getId())
                        && Objects.nonNull(aCategory.getCreatedAt())
                        && aCategory.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt())
                        && Objects.isNull(aUpdateCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "film";
        final var expectedDescription = "this category is most watch";
        final var expectedIsActive = true;
        final var expectedId = "123";

        final var expectedErrorMessage = "Category with ID 123 was not found";

        final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(Mockito.eq(CategoryID.from(expectedId))))
                .thenReturn(Optional.empty());

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, times(1)).findById(Mockito.eq(CategoryID.from(expectedId)));

        Mockito.verify(categoryGateway, times(0)).update(any());
    }
}
