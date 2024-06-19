package com.fullcycle.admin.catalogo.infrastructure.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import java.util.List;
import java.util.Objects;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalogo.ControllerTest;
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreOutput;
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GenreOutput;
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.GenreListOutput;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreOutput;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.entity.genre.Genre;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.models.CreateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.models.UpdateGenreRequest;

@ControllerTest(controllers = GenreAPI.class)
public class GenreAPITest {
    
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;
    
    @MockBean
    private CreateGenreUseCase createGenreUseCase;
    
    @MockBean
    private GetGenreByIdUseCase getGenreByIdUseCase;
    
    @MockBean
    private UpdateGenreUseCase updateGenreUseCase;
    
    @MockBean
    private DeleteGenreUseCase deleteGenreUseCase;
    
    @MockBean
    private ListGenreUseCase listGenreUseCase;

    //__________________________________________________________________________

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() throws Exception{
        //given
        final var expectedName = "Acao";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedId = "123";

        final var command = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        Mockito.when(createGenreUseCase.execute(Mockito.any()))
            .thenReturn(CreateGenreOutput.from(expectedId));
        
        //when
        final var request = MockMvcRequestBuilders.post("/genres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(command));
        
        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/genres/" + expectedId))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        // Mockito.verify(createGenreUseCase).execute(argThat(cmd -> 
        //     Objects.equals(expectedName, cmd.name())
        //     && Objects.equals(expectedCategories, cmd.categories())
        //     && Objects.equals(expectedIsActive, cmd.active())
        // ));
    }
    
    @Test
    public void givenAnInvalidName_whenCallsCreateGenre_shouldReturnNotification() throws Exception{
        //given
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var command = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        Mockito.when(createGenreUseCase.execute(any()))
            .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));
        
        //when
        final var request = MockMvcRequestBuilders.post("/genres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(command));
        
        final var reponse = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        //then
        reponse.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)))
                ;

        // Mockito.verify(createGenreUseCase).execute(argThat(cmd -> 
        //     Objects.equals(expectedName, cmd.name())
        //         && Objects.equals(expectedCategories, cmd.categories())
        //         && Objects.equals(expectedIsActive, cmd.active())
        // ));
    }

    @Test
    public void givenAValidId_whenCallsGetGenreByid_shouldReturnGenre() throws Exception{
        //given
        final var expectedName = "Acao";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = false;

        final var genre = Genre.newGenre(expectedName, expectedIsActive)
            .addCategory(expectedCategories.stream().map(CategoryID::from).toList());
        
        final var expectedId = genre.getId().getValue();

        Mockito.when(getGenreByIdUseCase.execute(Mockito.any())).thenReturn(GenreOutput.from(genre));

        //when
        final var request = MockMvcRequestBuilders.get("/genres/{id}", expectedId)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.categories_id", Matchers.equalTo(expectedCategories)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(genre.getCreatedAt().toString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(genre.getUpdatedAt().toString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.deleted_at", Matchers.equalTo(genre.getDeletedAt().toString())))
        ;

        Mockito.verify(getGenreByIdUseCase).execute(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetGenreByid_shouldReturnNotFound() throws Exception{
        //given
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");
        
        Mockito.when(getGenreByIdUseCase.execute(Mockito.any()))
        .thenThrow(NotFoundException.with(Genre.class, expectedId));

        //when
        final var request = MockMvcRequestBuilders.get("/genres/{id}", expectedId.getValue())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        //then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)))
        ;

        Mockito.verify(getGenreByIdUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() throws Exception{
        //given
        final var expectedName = "Acao";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = genre.getId().getValue();

        final var command = new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        Mockito.when(updateGenreUseCase.execute(Mockito.any())).thenReturn(UpdateGenreOutput.from(genre));
        
        //when
        final var request = MockMvcRequestBuilders.put("/genres/{id}", expectedId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(command));
        
        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        // Mockito.verify(updateGenreUseCase).execute(ArgumentMatchers.argThat(cmd -> 
        //     Objects.equals(expectedName, cmd.name())
        //     && Objects.equals(expectedCategories, cmd.categories())
        //     && Objects.equals(expectedIsActive, cmd.active())
        // ));
    }
    
    @Test
    public void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotification() throws Exception{
        //given
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var genre = Genre.newGenre("Acao", expectedIsActive);
        final var expectedId = genre.getId();

        final var command = new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        Mockito.when(updateGenreUseCase.execute(any()))
            .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));
        
        //when
        final var request = MockMvcRequestBuilders.put("/genres/{id}", expectedId.getValue())
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(command));
        
        final var reponse = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        //then
        reponse.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)))
                ;

        // Mockito.verify(updateGenreUseCase).execute(Mockito.argThat(cmd -> 
        //     Objects.equals(expectedName, cmd.name())
        //         && Objects.equals(expectedCategories, cmd.categories())
        //         && Objects.equals(expectedIsActive, cmd.active())
        // ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteGenre_shouldBeOK() throws Exception {
        //given
        final var expectedId = "123";

        Mockito.doNothing().when(deleteGenreUseCase).execute(Mockito.any());

        //when
        final var request = MockMvcRequestBuilders.delete("/genres/{id}", expectedId)
            .accept(MediaType.APPLICATION_JSON);

        final var result = this.mvc.perform(request);

        //then
        result.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(deleteGenreUseCase).execute(ArgumentMatchers.eq(expectedId));
    }

    @Test
    public void givenAVaidParams_whenCallsListGenres_shouldReturnGenres() throws Exception{
        //given
        final var genre = Genre.newGenre("Acao", false);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ac";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(GenreListOutput.from(genre));

        Mockito.when(listGenreUseCase.execute(Mockito.any()))
        .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        //when
        final var request = MockMvcRequestBuilders.get("/genres")
            .queryParam("page", String.valueOf(expectedPage))
            .queryParam("perPage", String.valueOf(expectedPerPage))
            .queryParam("sort", String.valueOf(expectedSort))
            .queryParam("dir", String.valueOf(expectedDirection))
            .queryParam("search", String.valueOf(expectedTerms))
            .accept(MediaType.APPLICATION_JSON);

        final var reponse = this.mvc.perform(request);
        
        //then
        reponse.andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(genre.getId().getValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(genre.getName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_active", Matchers.equalTo(genre.isActive())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(genre.getCreatedAt().toString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].deleted_at", Matchers.equalTo(genre.getDeletedAt().toString())))
            ;
    
        Mockito.verify(listGenreUseCase).execute(ArgumentMatchers.argThat(query -> 
            Objects.equals(expectedPage, query.page())
            && Objects.equals(expectedPerPage, query.perPage())
            && Objects.equals(expectedDirection, query.direction())
            && Objects.equals(expectedSort, query.sort())
            && Objects.equals(expectedTerms, query.terms())
        ));
    }
}