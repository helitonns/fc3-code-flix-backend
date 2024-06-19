package com.fullcycle.admin.catalogo.application.genre.update;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.entity.category.Category;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.entity.genre.Genre;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.persistence.GenreRepository;

@IntegrationTest
public class UpdateGenreUseCaseIT {

    @Autowired
    private UpdateGenreUseCase useCase;

    @SpyBean
    private CategoryGateway categoryGateway;

    @SpyBean
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    // __________________________________________________________________________

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreID() {
        // given
        final var genre = genreGateway.create(Genre.newGenre("acao", true));
        final var expectedId = genre.getId();

        final var expectedName = "Acao";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(expectedId.getValue(), expectedName,expectedIsActive, asString(expectedCategories));

        // when
        final var actualOutput = useCase.execute(command);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualGenre = genreRepository.findById(genre.getId().getValue()).get();
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
            expectedCategories.size() ==  actualGenre.getCategoryIDs().size() &&
            expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreID() {
        // given
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = categoryGateway.create(Category.newCategory("Series", null, true));
        
        final var genre = genreGateway.create(Genre.newGenre("acao", true));
        final var expectedId = genre.getId();

        final var expectedName = "Acao";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var command = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualOutput = useCase.execute(command);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualGenre = genreRepository.findById(genre.getId().getValue()).get();
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
            expectedCategories.size() ==  actualGenre.getCategoryIDs().size() &&
            expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreID() {
        // given
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = categoryGateway.create(Category.newCategory("Series", null, true));
        
        final var genre = genreGateway.create(Genre.newGenre("acao", true));
        final var expectedId = genre.getId();

        final var expectedName = "Acao";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var command = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualOutput = useCase.execute(command);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualGenre = genreRepository.findById(genre.getId().getValue()).get();
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
            expectedCategories.size() ==  actualGenre.getCategoryIDs().size() &&
            expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        // given
        final var genre = genreGateway.create(Genre.newGenre("acao", true));
        final var expectedId = genre.getId();

        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class,() -> useCase.execute(command));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        // given
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");

        final var genre = genreGateway.create(Genre.newGenre("acao", true));
        final var expectedId = genre.getId();

        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId(), series, documentarios);

        final var expectedErrorCount = 2;
        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be null";

        final var command = UpdateGenreCommand.with(expectedId.getValue(), expectedName,expectedIsActive, asString(expectedCategories));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class,() -> useCase.execute(command));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedCategories));
        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
    }

    // __________________________________________________________________________
    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream().map(CategoryID::getValue).toList();
    }
}
