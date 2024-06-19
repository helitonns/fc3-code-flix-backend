package com.fullcycle.admin.catalogo.application.genre.retrieve.get;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.entity.category.Category;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.entity.genre.Genre;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;

@IntegrationTest
public class GetGenreByIdUseCaseIT {
    
    @Autowired
    private GetGenreByIdUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;
    
    @Autowired
    private CategoryGateway categoryGateway;

    // __________________________________________________________________________

    @Test
    public void givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
        // given
        final var series = categoryGateway.create(Category.newCategory("Series", null, true));
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        
        final var expectedName = "Acao";
        final var expectedActive = true;
        final var expectedCategories = List.of(series.getId(), filmes.getId());

        final var genre = genreGateway.create(Genre.newGenre(expectedName, expectedActive).addCategory(expectedCategories));
        final var expectedId = genre.getId();

        // when
        final var actualGenre = useCase.execute(expectedId.getValue());

        // then
        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedActive, actualGenre.active());
        Assertions.assertTrue(
            expectedCategories.size() == actualGenre.categories().size() &&
            asString(expectedCategories).containsAll(actualGenre.categories())
        );
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(genre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), actualGenre.deletedAt());
    }

    @Test
    public void givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        // given
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        // when
        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    // __________________________________________________________________________
    private List<String> asString(final List<CategoryID> ids) {
        return ids.stream().map(CategoryID::getValue).toList();
    }
}
