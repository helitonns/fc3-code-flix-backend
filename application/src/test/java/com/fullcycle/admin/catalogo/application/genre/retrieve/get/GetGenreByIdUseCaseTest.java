package com.fullcycle.admin.catalogo.application.genre.retrieve.get;

import static org.mockito.Mockito.times;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.entity.genre.Genre;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;

public class GetGenreByIdUseCaseTest extends UseCaseTest {
    
    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    //__________________________________________________________________________
    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetGenre_shouldReturnGenre(){
        //given
        final var expectedName = "Acao";
        final var expectedActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"), CategoryID.from("456"));

        final var genre = Genre.newGenre(expectedName, expectedActive).addCategory(expectedCategories);
        final var expectedId = genre.getId();

        Mockito.when(genreGateway.findById(Mockito.any())).thenReturn(Optional.of(genre));

        //when
        final var actualGenre = useCase.execute(expectedId.getValue());

        //then
        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedActive, actualGenre.active());
        Assertions.assertEquals(asString(expectedCategories), actualGenre.categories());
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(genre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), actualGenre.deletedAt());

        Mockito.verify(genreGateway, times(1)).findById(Mockito.eq(expectedId));
    }
    
    @Test
    public void givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound(){
        //given
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        Mockito.when(genreGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.empty());

        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class, ()-> useCase.execute(expectedId.getValue()));

        //then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    //__________________________________________________________________________
    private List<String> asString(final List<CategoryID> ids){
        return ids.stream().map(CategoryID::getValue).toList();
    }
}