package com.fullcycle.admin.catalogo.application.genre.update;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.entity.genre.Genre;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;

class UpdateGenreUseCaseTest extends UseCaseTest{

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Override
        protected List<Object> getMocks() {
                return List.of(categoryGateway, genreGateway);
        }
    //__________________________________________________________________________

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreID(){
        //given
        final var genre = Genre.newGenre("acao", true);
        final var expectedId = genre.getId();

        final var expectedName = "Acao";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        Mockito.when(genreGateway.findById(Mockito.any())).thenReturn(Optional.of(Genre.with(genre)));
        Mockito.when(genreGateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        final var actualOutput = useCase.execute(command);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(updatedGenre ->
            Objects.equals(expectedId, updatedGenre.getId())
            && Objects.equals(expectedName, updatedGenre.getName())
            && Objects.equals(expectedIsActive, updatedGenre.isActive())
            && Objects.equals(expectedCategories, updatedGenre.getCategories())
            && Objects.equals(genre.getCreatedAt(), updatedGenre.getCreatedAt())
            && genre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())
            && Objects.isNull(updatedGenre.getDeletedAt())
        ));
    }
    
    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreID(){
        //given
        final var genre = Genre.newGenre("acao", true);
        final var expectedId = genre.getId();

        final var expectedName = "Acao";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var command = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        Mockito.when(genreGateway.findById(Mockito.any())).thenReturn(Optional.of(Genre.with(genre)));
        Mockito.when(genreGateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        final var actualOutput = useCase.execute(command);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(updatedGenre ->
            Objects.equals(expectedId, updatedGenre.getId())
            && Objects.equals(expectedName, updatedGenre.getName())
            && Objects.equals(expectedIsActive, updatedGenre.isActive())
            && Objects.equals(expectedCategories, updatedGenre.getCategories())
            && Objects.equals(genre.getCreatedAt(), updatedGenre.getCreatedAt())
            && genre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())
            && Objects.nonNull(updatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreID(){
        //given
        final var genre = Genre.newGenre("acao", true);
        final var expectedId = genre.getId();

        final var expectedName = "Acao";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"), CategoryID.from("456"));

        final var command = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        Mockito.when(genreGateway.findById(Mockito.any())).thenReturn(Optional.of(Genre.with(genre)));
        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(expectedCategories);
        Mockito.when(genreGateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        final var actualOutput = useCase.execute(command);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(expectedCategories);
        Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(updatedGenre ->
            Objects.equals(expectedId, updatedGenre.getId())
            && Objects.equals(expectedName, updatedGenre.getName())
            && Objects.equals(expectedIsActive, updatedGenre.isActive())
            && Objects.equals(expectedCategories, updatedGenre.getCategories())
            && Objects.equals(genre.getCreatedAt(), updatedGenre.getCreatedAt())
            && genre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())
            && Objects.isNull(updatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException(){
        //given
        final var genre = Genre.newGenre("acao", true);
        final var expectedId = genre.getId();

        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        Mockito.when(genreGateway.findById(Mockito.any())).thenReturn(Optional.of(Genre.with(genre)));

        //when
        final var actualException = Assertions.assertThrows(NotificationException.class,()-> useCase.execute(command));

        //then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException(){
        //given
        final var filmes = CategoryID.from("123");
        final var series = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");

        final var genre = Genre.newGenre("acao", true);
        final var expectedId = genre.getId();

        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes, series, documentarios);

        final var expectedErrorCount = 2;
        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be null";

        final var command = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        Mockito.when(genreGateway.findById(Mockito.any())).thenReturn(Optional.of(Genre.with(genre)));
        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(List.of(filmes));

        //when
        final var actualException = Assertions.assertThrows(NotificationException.class,()-> useCase.execute(command));

        //then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(expectedId);
        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedCategories));
        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
    }

    //__________________________________________________________________________
    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream().map(CategoryID::getValue).toList();
    }
}
