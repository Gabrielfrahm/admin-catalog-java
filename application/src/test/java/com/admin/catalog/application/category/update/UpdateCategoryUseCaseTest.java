package com.admin.catalog.application.category.update;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {
    @InjectMocks
    private DefaultUpdateUseCaseCategory useCase;

    @Mock
    private CategoryGateway categoryGateway;


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

        Mockito.when(categoryGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.of(aCategory));
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
}
