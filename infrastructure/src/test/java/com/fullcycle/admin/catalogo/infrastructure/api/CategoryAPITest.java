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
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.fullcycle.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import com.fullcycle.admin.catalogo.application.category.retrieve.list.ListCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.entity.category.Category;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import com.fullcycle.admin.catalogo.infrastructure.entity.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.entity.category.models.UpdateCategoryRequest;
import io.vavr.API;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper mapper;

        @MockBean
        private CreateCategoryUseCase createCategoryUseCase;

        @MockBean
        private GetCategoryByIdUseCase getCategoryByIdUseCase;

        @MockBean
        private UpdateCategoryUseCase updateCategoryUseCase;

        @MockBean
        private DeleteCategoryUseCase deleteCategoryUseCase;

        @MockBean
        private ListCategoryUseCase listCategoryUseCase;

        @Test
        public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
                // given
                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                final var input = new CreateCategoryRequest(expectedName, expectedDescription,
                                expectedIsActive);

                Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                                .thenReturn(API.Right(CreateCategoryOutput.from("123")));

                // when
                final var request = MockMvcRequestBuilders.post("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.mapper.writeValueAsString(input));


                final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

                // then
                response.andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.header().string("Location",
                                                "/categories/123"))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                                                Matchers.equalTo("123")));

                Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(
                                Mockito.argThat(cmd -> Objects.equals(expectedName, cmd.name())
                                                && Objects.equals(expectedDescription,
                                                                cmd.description())
                                                && Objects.equals(expectedIsActive,
                                                                cmd.isActive())));
        }

        @Test
        public void givenAInvalidName_whenCallsCreateCategory_shouldReturnNotification()
                        throws Exception {
                // given
                final String expectedName = null;
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;
                final var expectedMessage = "'name' should not be null";

                final var input = new CreateCategoryRequest(expectedName, expectedDescription,
                                expectedIsActive);

                Mockito.when(createCategoryUseCase.execute(Mockito.any())).thenReturn(
                                API.Left(Notification.create(new Error(expectedMessage))));

                // when
                final var request = MockMvcRequestBuilders.post("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.mapper.writeValueAsString(input));

                final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

                // then
                response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                                .andExpect(MockMvcResultMatchers.header().string("Location",
                                                Matchers.nullValue()))
                                .andExpect(MockMvcResultMatchers.header().string("Content-Type",
                                                MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors",
                                                Matchers.hasSize(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message",
                                                Matchers.equalTo(expectedMessage)));

                Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(
                                Mockito.argThat(cmd -> Objects.equals(expectedName, cmd.name())
                                                && Objects.equals(expectedDescription,
                                                                cmd.description())
                                                && Objects.equals(expectedIsActive,
                                                                cmd.isActive())));
        }

        @Test
        public void givenAInvalidCommand_whenCallsCreateCategory_shouldReturnDomainException()
                        throws Exception {
                // give
                final String expectedName = null;
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;
                final var expectedMessage = "'name' should not be null";

                final var input = new CreateCategoryRequest(expectedName, expectedDescription,
                                expectedIsActive);

                Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                                .thenThrow(DomainException.with(new Error(expectedMessage)));

                // when
                final var request = MockMvcRequestBuilders.post("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.mapper.writeValueAsString(input));

                final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

                // then
                response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                                .andExpect(MockMvcResultMatchers.header().string("Location",
                                                Matchers.nullValue()))
                                .andExpect(MockMvcResultMatchers.header().string("Content-Type",
                                                MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors",
                                                Matchers.hasSize(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message",
                                                Matchers.equalTo(expectedMessage)));

                Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(
                                Mockito.argThat(cmd -> Objects.equals(expectedName, cmd.name())
                                                && Objects.equals(expectedDescription,
                                                                cmd.description())
                                                && Objects.equals(expectedIsActive,
                                                                cmd.isActive())));
        }

        @Test
        public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception {
                // given
                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                final var aCategory = Category.newCategory(expectedName, expectedDescription,
                                expectedIsActive);
                final var expectedId = aCategory.getId().getValue();

                Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                                .thenReturn(CategoryOutput.from(aCategory));

                // when
                final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON);

                final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

                // then
                response.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                                                Matchers.equalTo(expectedId)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name",
                                                Matchers.equalTo(expectedName)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.description",
                                                Matchers.equalTo(expectedDescription)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active",
                                                Matchers.equalTo(expectedIsActive)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at",
                                                Matchers.equalTo(aCategory.getCreatedAt()
                                                                .toString())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at",
                                                Matchers.equalTo(aCategory.getUpdatedAt()
                                                                .toString())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted_at",
                                                Matchers.equalTo(aCategory.getDeletedAt())));

                Mockito.verify(getCategoryByIdUseCase, Mockito.times(1))
                                .execute(Mockito.eq(expectedId));
        }

        @Test
        public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception {
                // given
                final var expectedMessage = "Category with ID 123 was not found";
                final var expectedId = CategoryID.from("123");

                Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                                .thenThrow(NotFoundException.with(Category.class, expectedId));

                // when
                final var request = MockMvcRequestBuilders
                                .get("/categories/{id}", expectedId.getValue())
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON);;

                final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

                // then
                response.andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                Matchers.equalTo(expectedMessage)));

        }

        @Test
        public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId()
                        throws Exception {
                // given
                final var expectedId = "123";
                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                                .thenReturn(API.Right(UpdateCategoryOutput.from(expectedId)));

                final var command = new UpdateCategoryRequest(expectedName, expectedDescription,
                                expectedIsActive);

                // when
                final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(command));

                final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

                // then
                response.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.header().string("Content-Type",
                                                MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                                                Matchers.equalTo(expectedId)));

                Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(
                                Mockito.argThat(cmd -> Objects.equals(expectedName, cmd.name())
                                                && Objects.equals(expectedDescription,
                                                                cmd.description())
                                                && Objects.equals(expectedIsActive,
                                                                cmd.isActive())));
        }

        @Test
        public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException()
                        throws Exception {
                // given
                final var expectedId = "123";
                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                final var expectedErrorMessage = "Category with ID 123 was not found";

                Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                                .thenThrow(NotFoundException.with(Category.class,
                                                CategoryID.from(expectedId)));

                final var command = new UpdateCategoryRequest(expectedName, expectedDescription,
                                expectedIsActive);

                // when
                final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(command));

                final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

                // then
                response.andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.header().string("Content-Type",
                                                MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                Matchers.equalTo(expectedErrorMessage)));

                Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(
                                Mockito.argThat(cmd -> Objects.equals(expectedName, cmd.name())
                                                && Objects.equals(expectedDescription,
                                                                cmd.description())
                                                && Objects.equals(expectedIsActive,
                                                                cmd.isActive())));
        }

        @Test
        public void givenAInvalidName_whenCallsUpdateCategory_shouldReturnDomainException()
                        throws Exception {
                // given
                final var expectedId = "123";
                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                final var expectedErrorMessage = "'name' should not be null";
                final var expectedErrorCount = 1;

                Mockito.when(updateCategoryUseCase.execute(Mockito.any())).thenReturn(
                                API.Left(Notification.create(new Error(expectedErrorMessage))));

                final var command = new UpdateCategoryRequest(expectedName, expectedDescription,
                                expectedIsActive);

                // when
                final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(command));

                final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

                // then
                response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                                .andExpect(MockMvcResultMatchers.header().string("Content-Type",
                                                MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors",
                                                Matchers.hasSize(expectedErrorCount)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message",
                                                Matchers.equalTo(expectedErrorMessage)));

                Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(
                                Mockito.argThat(cmd -> Objects.equals(expectedName, cmd.name())
                                                && Objects.equals(expectedDescription,
                                                                cmd.description())
                                                && Objects.equals(expectedIsActive,
                                                                cmd.isActive())));
        }

        @Test
        public void givenAValidId_whenCallsDeleteCategory_shouldReturnNoContent() throws Exception {
                // given
                final var expectedId = "123";

                Mockito.doNothing().when(deleteCategoryUseCase).execute(Mockito.any());

                // when
                final var request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON);

                final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

                // then
                response.andExpect(MockMvcResultMatchers.status().isNoContent());

                Mockito.verify(deleteCategoryUseCase, Mockito.times(1))
                                .execute(Mockito.eq(expectedId));
        }

        @Test
        public void givenAValidParams_whenCallsListCategories_shouldReturnCategories()
                        throws Exception {
                // given
                final var category = Category.newCategory("Movies", null, true);
                final var expectedPage = 0;
                final var expectedPerPage = 10;
                final var expectedTerms = "movies";
                final var expectedShort = "description";
                final var expectedDirection = "desc";
                final var expectedItemsCount = 1;
                final var expectedTotal = 1;
                final var expectedItems = List.of(CategoryListOutput.from(category));

                // final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms,
                // expectedShort, expectedDirection);

                Mockito.when(listCategoryUseCase.execute(Mockito.any()))
                                .thenReturn(new Pagination<>(expectedPage, expectedPerPage,
                                                expectedTotal, expectedItems));

                // when
                final var request = MockMvcRequestBuilders.get("/categories")
                                .queryParam("page", String.valueOf(expectedPage))
                                .queryParam("perPage", String.valueOf(expectedPerPage))
                                .queryParam("sort", expectedShort)
                                .queryParam("dir", expectedDirection)
                                .queryParam("search", expectedTerms)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON);

                final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

                // then
                response.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page",
                                                Matchers.equalTo(expectedPage)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page",
                                                Matchers.equalTo(expectedPerPage)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.total",
                                                Matchers.equalTo(expectedTotal)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items",
                                                Matchers.hasSize(expectedItemsCount)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id",
                                                Matchers.equalTo(category.getId().getValue())));

                Mockito.verify(listCategoryUseCase, Mockito.times(1)).execute(
                                Mockito.argThat(query -> Objects.equals(expectedPage, query.page())
                                                && Objects.equals(expectedPerPage, query.perPage())
                                                && Objects.equals(expectedDirection,
                                                                query.direction())
                                                && Objects.equals(expectedShort, query.sort())
                                                && Objects.equals(expectedTerms, query.terms())));

        }

}


