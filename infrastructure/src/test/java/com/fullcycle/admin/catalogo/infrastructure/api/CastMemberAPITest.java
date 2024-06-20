package com.fullcycle.admin.catalogo.infrastructure.api;

import java.util.List;
import java.util.Objects;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
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
import com.fullcycle.admin.catalogo.Fixture;
import com.fullcycle.admin.catalogo.application.castmember.create.CreateCastMemberOutput;
import com.fullcycle.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.get.CastMemberOutput;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.list.CastMemberListOutput;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.fullcycle.admin.catalogo.application.castmember.update.UpdateCastMemberOutput;
import com.fullcycle.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.models.CreateCastMemberRequest;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.models.UpdateCastMemberRequest;

@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCastMemberUseCase createCastMemberUseCase;

    @MockBean
    private DeleteCastMemberUseCase deleteCastMemberUseCase;
    
    @MockBean
    private GetCastMemberByIdUseCase getCastMemberByIdUseCase;
    
    @MockBean
    private ListCastMembersUseCase listCastMembersUseCase;
    
    @MockBean
    private UpdateCastMemberUseCase updateCastMemberUseCase;
    //__________________________________________________________________________

    @Test
    public void giverAValidCommand_whenCallsCreateCastMember_shouldReturnItsIdentifier() throws Exception{
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var expectedId = CastMemberID.from("123");

        final var command = new CreateCastMemberRequest(expectedName, expectedType);

        Mockito.when(createCastMemberUseCase.execute(Mockito.any())).thenReturn(CreateCastMemberOutput.from(expectedId));
        
        //when
        final var request = MockMvcRequestBuilders.post("/cast_members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());
        
        //then
        response
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.header().string("Location", "/cast_members/" + expectedId.getValue()))
            .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        Mockito.verify(createCastMemberUseCase).execute(Mockito.argThat(cmd -> 
            Objects.equals(expectedName, cmd.name())
            && Objects.equals(expectedType, cmd.type())));
    }

    @Test
    public void giverAnInvalidName_whenCallsCreateCastMember_shouldReturnNotification() throws Exception{
        //given
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "'name' should not be null";

        final var command = new CreateCastMemberRequest(expectedName, expectedType);

        Mockito.when(createCastMemberUseCase.execute(Mockito.any()))
            .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        //when
        final var request = MockMvcRequestBuilders.post("/cast_members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());
        
        //then
        response
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
            .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
            .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage))
        );

        Mockito.verify(createCastMemberUseCase).execute(Mockito.argThat(cmd -> 
            Objects.equals(expectedName, cmd.name())
            && Objects.equals(expectedType, cmd.type())));
    }

    @Test
    public void givenAValidaId_whenCallsGetById_shouldReturnIt() throws Exception{
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId().getValue();

        Mockito.when(getCastMemberByIdUseCase.execute(Mockito.any())).thenReturn(CastMemberOutput.from(member));
        
        //whehn
        final var request = MockMvcRequestBuilders.get("/cast_members/{id}", expectedId)
            .accept(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request);

        //then
        response
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo(expectedType.name())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(member.getCreatedAt().toString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(member.getUpdatedAt().toString())));

        Mockito.verify(getCastMemberByIdUseCase).execute(Mockito.eq(expectedId));
    }

    @Test
    public void givenAnInvalidaId_whenCallsGetByIdAndCastMemberDoesntExist_shouldReturnNotFound() throws Exception{
        //given
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedId = CastMemberID.from("123");

        Mockito.when(getCastMemberByIdUseCase.execute(Mockito.any()))
            .thenThrow(NotFoundException.with(CastMember.class, expectedId));
        
        //whehn
        final var request = MockMvcRequestBuilders.get("/cast_members/{id}", expectedId.getValue())
            .accept(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request);

        //then
        response
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(getCastMemberByIdUseCase).execute(Mockito.eq(expectedId.getValue()));
    }

    @Test
    public void giverAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() throws Exception{
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();

        final var command = new UpdateCastMemberRequest(expectedName, expectedType);

        Mockito.when(updateCastMemberUseCase.execute(Mockito.any())).thenReturn(UpdateCastMemberOutput.from(expectedId));
        
        //when
        final var request = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId.getValue())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());
        
        //then
        response
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        Mockito.verify(updateCastMemberUseCase).execute(Mockito.argThat(cmd -> 
            Objects.equals(expectedId.getValue(), cmd.id())
            && Objects.equals(expectedName, cmd.name())
            && Objects.equals(expectedType, cmd.type())));
    }

    @Test
    public void giverAnInvalidName_whenCallsUpdateCastMember_shouldReturnNotification() throws Exception{
        //given
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "'name' should not be null";
        final var member = CastMember.newMember("Jao", expectedType);
        final var expectedId = member.getId();

        final var command = new UpdateCastMemberRequest(expectedName, expectedType);

        Mockito.when(updateCastMemberUseCase.execute(Mockito.any()))
            .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        //when
        final var request = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId.getValue())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());
        
        //then
        response
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
            .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
            .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage))
        );

        Mockito.verify(updateCastMemberUseCase).execute(Mockito.argThat(cmd -> 
            Objects.equals(expectedId.getValue(), cmd.id())
            && Objects.equals(expectedName, cmd.name())
            && Objects.equals(expectedType, cmd.type())));
    }
    
    @Test
    public void giverAnInvalidId_whenCallsUpdateCastMember_shouldReturnNotFound() throws Exception{
        //given
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedId = CastMemberID.from("123");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var command = new UpdateCastMemberRequest(expectedName, expectedType);

        Mockito.when(updateCastMemberUseCase.execute(Mockito.any()))
            .thenThrow(NotFoundException.with(CastMember.class, expectedId));

        //when
        final var request = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId.getValue())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());
        
        //then
        response
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
            .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage))
        );

        Mockito.verify(updateCastMemberUseCase).execute(Mockito.argThat(cmd -> 
            Objects.equals(expectedId.getValue(), cmd.id())
            && Objects.equals(expectedName, cmd.name())
            && Objects.equals(expectedType, cmd.type())));
    }

    @Test
    public void givenAValidId_whenCallsDeleteById_shouldDeletedIt() throws Exception{
        //given
        final var expectedId = "123";

        Mockito.doNothing().when(deleteCastMemberUseCase).execute(Mockito.any());
        
        //when
        final var request = MockMvcRequestBuilders.delete("/cast_members/{id}", expectedId)
            .accept(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request);

        //then
        response.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(deleteCastMemberUseCase).execute(Mockito.eq(expectedId));
    }

    @Test
    public void givenAValidParams_whenCallsListCastMembers_shouldReturnIt() throws Exception{
        //given
        final var member = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        final var expectedPage = 1;
        final var expectedPerPage = 20;
        final var expectedTerms = "";
        final var expectedSort = "type";
        final var expectedDirection = "desc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(CastMemberListOutput.from(member));

        Mockito.when(listCastMembersUseCase.execute(Mockito.any()))
            .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        //when
        final var request = MockMvcRequestBuilders.get("/cast_members")
            .queryParam("page", String.valueOf(expectedPage))
            .queryParam("perPage", String.valueOf(expectedPerPage))
            .queryParam("search", expectedTerms)
            .queryParam("sort", expectedSort)
            .queryParam("dir", expectedDirection)
            .accept(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request);

        //then
        response
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(member.getId().getValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(member.getName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].type", Matchers.equalTo(member.getType().name())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(member.getCreatedAt().toString())));
        
            Mockito.verify(listCastMembersUseCase).execute(Mockito.argThat(query -> 
                Objects.equals(expectedPage, query.page())
                && Objects.equals(expectedPerPage, query.perPage())
                && Objects.equals(expectedTerms, query.terms())
                && Objects.equals(expectedSort, query.sort())
                && Objects.equals(expectedDirection, query.direction())
            ));
    }
    
    @Test
    public void givenEmptyParams_whenCallsListCastMembers_shouldUseDefaultsAndReturnIt() throws Exception{
        //given
        final var member = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(CastMemberListOutput.from(member));

        Mockito.when(listCastMembersUseCase.execute(Mockito.any()))
            .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        //when
        final var request = MockMvcRequestBuilders.get("/cast_members")
            .accept(MediaType.APPLICATION_JSON);

        final var response = mvc.perform(request);

        //then
        response
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(member.getId().getValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(member.getName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].type", Matchers.equalTo(member.getType().name())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(member.getCreatedAt().toString())));
        
            Mockito.verify(listCastMembersUseCase).execute(Mockito.argThat(query -> 
                Objects.equals(expectedPage, query.page())
                && Objects.equals(expectedPerPage, query.perPage())
                && Objects.equals(expectedTerms, query.terms())
                && Objects.equals(expectedSort, query.sort())
                && Objects.equals(expectedDirection, query.direction())
            ));
    }
}