package com.fullcycle.admin.catalogo.application.genre.create;

import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;

public class CreateGenreUseCaseTest extends UseCaseTest {

        @InjectMocks
        private DefaultCreateGenreUseCase useCase;

        @Mock
        private CategoryGateway categoryGateway;

        @Mock
        private GenreGateway genreGateway;

        @Override
        protected List<Object> getMocks() {
                return List.of(categoryGateway, genreGateway);
        }
        // __________________________________________________________________________

        @Test
        public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreID() {
                // given
                final var expectName = "Acao";
                final var expectIsActive = true;
                final var expectCategories = List.<CategoryID>of();

                final var command = CreateGenreCommand.with(expectName, expectIsActive,
                                asString(expectCategories));

                Mockito.when(genreGateway.create(Mockito.any()))
                                .thenAnswer(AdditionalAnswers.returnsFirstArg());

                // when
                final var actualOutput = useCase.execute(command);

                // then
                Assertions.assertNotNull(actualOutput);
                Assertions.assertNotNull(actualOutput.id());

                Mockito.verify(genreGateway, Mockito.times(1)).create(
                                Mockito.argThat(genre -> Objects.equals(expectName, genre.getName())
                                                && Objects.equals(expectIsActive, genre.isActive())
                                                && Objects.equals(expectCategories,
                                                                genre.getCategories())
                                                && Objects.nonNull(genre.getId())
                                                && Objects.nonNull(genre.getCreatedAt())
                                                && Objects.nonNull(genre.getUpdatedAt())
                                                && Objects.isNull(genre.getDeletedAt())));
        }

        @Test
        public void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreID() {
                // given
                final var expectName = "Acao";
                final var expectIsActive = false;
                final var expectCategories = List.<CategoryID>of();

                final var command = CreateGenreCommand.with(expectName, expectIsActive,
                                asString(expectCategories));

                Mockito.when(genreGateway.create(Mockito.any()))
                                .thenAnswer(AdditionalAnswers.returnsFirstArg());

                // when
                final var actualOutput = useCase.execute(command);

                // then
                Assertions.assertNotNull(actualOutput);
                Assertions.assertNotNull(actualOutput.id());

                Mockito.verify(genreGateway, Mockito.times(1)).create(
                                Mockito.argThat(genre -> Objects.equals(expectName, genre.getName())
                                                && Objects.equals(expectIsActive, genre.isActive())
                                                && Objects.equals(expectCategories,
                                                                genre.getCategories())
                                                && Objects.nonNull(genre.getId())
                                                && Objects.nonNull(genre.getCreatedAt())
                                                && Objects.nonNull(genre.getUpdatedAt())
                                                && Objects.nonNull(genre.getDeletedAt())));
        }

        @Test
        public void givenAValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreId() {
                // given
                final var expectName = "Acao";
                final var expectIsActive = true;
                final var expectCategories =
                                List.of(CategoryID.from("123"), CategoryID.from("456"));

                final var command = CreateGenreCommand.with(expectName, expectIsActive,
                                asString(expectCategories));

                Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                                .thenReturn(expectCategories);
                Mockito.when(genreGateway.create(Mockito.any()))
                                .thenAnswer(AdditionalAnswers.returnsFirstArg());

                // when
                final var actualOutput = useCase.execute(command);

                // then
                Assertions.assertNotNull(actualOutput);
                Assertions.assertNotNull(actualOutput.id());

                Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(expectCategories);
                Mockito.verify(genreGateway, Mockito.times(1)).create(
                                Mockito.argThat(genre -> Objects.equals(expectName, genre.getName())
                                                && Objects.equals(expectIsActive, genre.isActive())
                                                && Objects.equals(expectCategories,
                                                                genre.getCategories())
                                                && Objects.nonNull(genre.getId())
                                                && Objects.nonNull(genre.getCreatedAt())
                                                && Objects.nonNull(genre.getUpdatedAt())
                                                && Objects.isNull(genre.getDeletedAt())));
        }

        @Test
        public void givenAnInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException() {
                // given
                final var expectName = " ";
                final var expectIsActive = true;
                final var expectCategories = List.<CategoryID>of();
                final var expectedErrorMessage = "'name' should not be empty";
                final var expectedErrorCount = 1;

                final var command = CreateGenreCommand.with(expectName, expectIsActive,
                                asString(expectCategories));

                // when
                final var actualException = Assertions.assertThrows(NotificationException.class,
                                () -> useCase.execute(command));

                // then
                Assertions.assertNotNull(actualException);
                Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
                Assertions.assertEquals(expectedErrorMessage,
                                actualException.getErrors().get(0).message());

                Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
                Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
        }

        @Test
        public void givenAnInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException() {
                // given
                final String expectName = null;
                final var expectIsActive = true;
                final var expectCategories = List.<CategoryID>of();
                final var expectedErrorMessage = "'name' should not be null";
                final var expectedErrorCount = 1;

                final var command = CreateGenreCommand.with(expectName, expectIsActive,
                                asString(expectCategories));

                // when
                final var actualException = Assertions.assertThrows(NotificationException.class,
                                () -> useCase.execute(command));

                // then
                Assertions.assertNotNull(actualException);
                Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
                Assertions.assertEquals(expectedErrorMessage,
                                actualException.getErrors().get(0).message());

                Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
                Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
        }

        @Test
        public void givenAValidCommand_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
                // given
                final var series = CategoryID.from("123");
                final var filmes = CategoryID.from("456");
                final var documentarios = CategoryID.from("789");

                final String expectName = "Acao";
                final var expectIsActive = true;
                final var expectCategories = List.of(series, filmes, documentarios);
                final var expectedErrorMessage = "Some categories could not be found: 456, 789";
                final var expectedErrorCount = 1;

                Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                                .thenReturn(List.of(series));

                final var command = CreateGenreCommand.with(expectName, expectIsActive,
                                asString(expectCategories));

                // when
                final var actualException = Assertions.assertThrows(NotificationException.class,
                                () -> useCase.execute(command));

                // then
                Assertions.assertNotNull(actualException);
                Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
                Assertions.assertEquals(expectedErrorMessage,
                                actualException.getErrors().get(0).message());

                Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.any());
                Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
        }

        @Test
        public void givenAnInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
                // given
                final var series = CategoryID.from("123");
                final var filmes = CategoryID.from("456");
                final var documentarios = CategoryID.from("789");

                final String expectName = " ";
                final var expectIsActive = true;
                final var expectCategories = List.of(series, filmes, documentarios);
                final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
                final var expectedErrorMessageTwo = "'name' should not be empty";
                final var expectedErrorCount = 2;

                Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                                .thenReturn(List.of(series));

                final var command = CreateGenreCommand.with(expectName, expectIsActive,
                                asString(expectCategories));

                // when
                final var actualException = Assertions.assertThrows(NotificationException.class,
                                () -> useCase.execute(command));

                // then
                Assertions.assertNotNull(actualException);
                Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
                Assertions.assertEquals(expectedErrorMessageOne,
                                actualException.getErrors().get(0).message());
                Assertions.assertEquals(expectedErrorMessageTwo,
                                actualException.getErrors().get(1).message());

                Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.any());
                Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
        }

}
