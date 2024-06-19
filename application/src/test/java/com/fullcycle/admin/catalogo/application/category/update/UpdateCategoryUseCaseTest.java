package com.fullcycle.admin.catalogo.application.category.update;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.entity.category.Category;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;

public class UpdateCategoryUseCaseTest extends UseCaseTest {
        @Mock
        private CategoryGateway categoryGateway;

        @InjectMocks
        private DefaultUpdateCategoryUseCase useCase;

        @Override
        protected List<Object> getMocks() {
                return List.of(categoryGateway);
        }

        // ______________________________________________________________________________
        // 1. Teste do caminho feliz
        // 2. Teste passando uma propriedade invalida (name)
        // 3. Teste atualizandp uma categoria para inativa
        // 4. Teste simulando um erro generico vindo do gateway
        // 5. Teste atualizando categoria passando ID invalido
        // ______________________________________________________________________________

        @Test
        public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
                final var aCategory = Category.newCategory("Film", null, true);

                final var expectedId = aCategory.getId();
                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName,
                                expectedDescription, expectedIsActive);

                Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                                .thenReturn(Optional.of(aCategory.clone()));
                Mockito.when(categoryGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

                final var actualOutput = useCase.execute(aCommand).get();

                Assertions.assertNotNull(actualOutput.id());

                Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
                Mockito.verify(categoryGateway, Mockito.times(1))
                                .update(Mockito.argThat(aUpdateCategory -> Objects
                                                .equals(expectedName, aUpdateCategory.getName())
                                                && Objects.equals(expectedDescription,
                                                                aUpdateCategory.getDescription())
                                                && Objects.equals(expectedIsActive,
                                                                aUpdateCategory.isActive())
                                                && Objects.equals(expectedId,
                                                                aUpdateCategory.getId())
                                                && Objects.equals(aCategory.getCreatedAt(),
                                                                aUpdateCategory.getCreatedAt())
                                                && aCategory.getUpdatedAt().isBefore(
                                                                aUpdateCategory.getUpdatedAt())
                                                && Objects.isNull(aUpdateCategory.getDeletedAt())));

        }

        @Test
        public void givenAInvalidName_whenCallsUpdateCategory_shouldReturnDomainException() {
                final var aCategory = Category.newCategory("Film", null, true);

                final var expectedId = aCategory.getId();
                final String expectedName = null;
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;
                final var expectedErrorMessage = "'name' should not be null";
                final var expectedErrorCount = 1;

                // O command é um record com contém as informações da categoria
                final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName,
                                expectedDescription, expectedIsActive);
                Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                                .thenReturn(Optional.of(aCategory.clone()));

                final var notification = useCase.execute(aCommand).getLeft();

                Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
                Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

                Mockito.verify(categoryGateway, Mockito.times(0)).update(Mockito.any());
        }

        @Test
        public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
                final var aCategory = Category.newCategory("Film", null, true);

                final var expectedId = aCategory.getId();
                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = false;

                final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName,
                                expectedDescription, expectedIsActive);

                Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                                .thenReturn(Optional.of(aCategory.clone()));
                Mockito.when(categoryGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

                Assertions.assertTrue(aCategory.isActive());
                Assertions.assertNull(aCategory.getDeletedAt());

                final var actualOutput = useCase.execute(aCommand).get();

                Assertions.assertNotNull(actualOutput);
                Assertions.assertNotNull(actualOutput.id());

                Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
                Mockito.verify(categoryGateway, Mockito.times(1))
                                .update(Mockito.argThat(aUpdateCategory -> Objects
                                                .equals(expectedName, aUpdateCategory.getName())
                                                && Objects.equals(expectedDescription,
                                                                aUpdateCategory.getDescription())
                                                && Objects.equals(expectedIsActive,
                                                                aUpdateCategory.isActive())
                                                && Objects.equals(expectedId,
                                                                aUpdateCategory.getId())
                                                && Objects.equals(aCategory.getCreatedAt(),
                                                                aUpdateCategory.getCreatedAt())
                                                && aCategory.getUpdatedAt().isBefore(
                                                                aUpdateCategory.getUpdatedAt())
                                                && Objects.nonNull(
                                                                aUpdateCategory.getDeletedAt())));

        }

        @Test
        public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
                final var aCategory = Category.newCategory("Film", null, true);

                final var expectedId = aCategory.getId();
                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;
                final var expectedErrorMessage = "Gateway error";
                final var expectedErrorCount = 1;

                final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName,
                                expectedDescription, expectedIsActive);

                Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
                                .thenReturn(Optional.of(aCategory.clone()));
                Mockito.when(categoryGateway.update(Mockito.any()))
                                .thenThrow(new IllegalStateException(expectedErrorMessage));

                final var notification = useCase.execute(aCommand).getLeft();

                Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
                Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

                Mockito.verify(categoryGateway, Mockito.times(1))
                                .update(Mockito.argThat(aUpdateCategory -> Objects
                                                .equals(expectedName, aUpdateCategory.getName())
                                                && Objects.equals(expectedDescription,
                                                                aUpdateCategory.getDescription())
                                                && Objects.equals(expectedIsActive,
                                                                aUpdateCategory.isActive())
                                                && Objects.equals(expectedId,
                                                                aUpdateCategory.getId())
                                                && Objects.equals(aCategory.getCreatedAt(),
                                                                aUpdateCategory.getCreatedAt())
                                                && aCategory.getUpdatedAt().isBefore(
                                                                aUpdateCategory.getUpdatedAt())
                                                && Objects.isNull(aUpdateCategory.getDeletedAt())));
        }

        @Test
        public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
                final var expectedId = "123";
                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = false;
                // final var expectedErrorCount = 1;
                final var expectedErrorMessage = "Category with ID 123 was not found";

                final var aCommand = UpdateCategoryCommand.with(expectedId, expectedName,
                                expectedDescription, expectedIsActive);

                Mockito.when(categoryGateway.findById(Mockito.eq(CategoryID.from(expectedId))))
                                .thenReturn(Optional.empty());

                final var actualException = Assertions.assertThrows(NotFoundException.class,
                                () -> useCase.execute(aCommand));

                Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

                Mockito.verify(categoryGateway, Mockito.times(1))
                                .findById(Mockito.eq(CategoryID.from(expectedId)));
                Mockito.verify(categoryGateway, Mockito.times(0)).update(Mockito.any());

        }
}


