package com.fullcycle.admin.catalogo.e2e;

import java.util.List;
import java.util.function.Function;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreID;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.models.CastMemberResponse;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.models.CreateCastMemberRequest;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.models.UpdateCastMemberRequest;
import com.fullcycle.admin.catalogo.infrastructure.entity.category.models.CategoryResponse;
import com.fullcycle.admin.catalogo.infrastructure.entity.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.entity.category.models.UpdateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.models.CreateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.models.GenreResponse;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.models.UpdateGenreRequest;

public interface MockDsl {

    MockMvc mvc();
    //__________________________________________________________________________
    
    /**
     * Cast Member
     */
    default CastMemberID givenACastMember(final String name, final CastMemberType type) throws Exception {
        final var body = new CreateCastMemberRequest(name, type);
        final var actualId = this.given("/cast_members", body);
        return CastMemberID.from(actualId);
    }
    
    default ResultActions givenACastMemberResult(final String name, final CastMemberType type) throws Exception {
        final var body = new CreateCastMemberRequest(name, type);
        return this.givenResult("/cast_members", body);
    }

    default ResultActions listCastMembers(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        return list("/cast_members", page, perPage, search, sort, direction);
    }

    default ResultActions listCastMembers(final int page, final int perPage) throws Exception {
        return listCastMembers(page, perPage, "", "", "");
    }

    default ResultActions listCastMembers(final int page, final int perPage, final String search) throws Exception {
        return listCastMembers(page, perPage, search, "", "");
    }

    default CastMemberResponse retrieveACastMember(final CastMemberID id) throws Exception {
        return retrieve("/cast_members/", id, CastMemberResponse.class);
    }
    
    default ResultActions retrieveACastMemberResult(final CastMemberID id) throws Exception {
        return retrieveResult("/cast_members/", id);
    }
    
    default ResultActions updateACastMember(final CastMemberID id, final String name, final CastMemberType type) throws Exception {
        return this.update("/cast_members/", id, new UpdateCastMemberRequest(name, type));
    }

    default ResultActions deleteACastMember(final CastMemberID id) throws Exception {
        return this.delete("/cast_members/", id);
    }
    //__________________________________________________________________________

    /**
     * Category
     */
    default CategoryID givenAValidCategoryID(final String name, final String description,
        final boolean isActive) throws Exception {
        final var requestBody = new CreateCategoryRequest(name, description, isActive);
        final var actualId = this.given("/categories", requestBody);
        return CategoryID.from(actualId);
    }

    default ResultActions listCategories(final int page, final int perPage, final String search,
            final String sort, final String direction) throws Exception {
        return list("/categories", page, perPage, search, sort, direction);
    }

    default ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    default ResultActions listCategories(final int page, final int perPage, final String search)
            throws Exception {
        return listCategories(page, perPage, search, "", "");
    }

    default CategoryResponse retrieveACetegory(final CategoryID id) throws Exception {
        return retrieve("/categories/", id, CategoryResponse.class);
    }

    default ResultActions updateACategory(final CategoryID id, final UpdateCategoryRequest body) throws Exception {
        return this.update("/categories/", id, body);
    }
    
    default ResultActions deleteACategory(final CategoryID id) throws Exception {
        return this.delete("/categories/", id);
    }
    // __________________________________________________________________________
    
    /**
     * Genre
     */
    default GenreID givenAGenre(final String name, final List<CategoryID> categories,
            final boolean isActive) throws Exception {
        final var requestBody =
                new CreateGenreRequest(name, mapTo(categories, CategoryID::getValue), isActive);
        final var actualId = this.given("/genres", requestBody);
        return GenreID.from(actualId);
    }

    default ResultActions listGenres(final int page, final int perPage, final String search,
            final String sort, final String direction) throws Exception {
        return list("/genres", page, perPage, search, sort, direction);
    }

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return listGenres(page, perPage, "", "", "");
    }

    default ResultActions listGenres(final int page, final int perPage, final String search)
            throws Exception {
        return listGenres(page, perPage, search, "", "");
    }

    default GenreResponse retrieveAGenre(final GenreID id) throws Exception {
        return retrieve("/genres/", id, GenreResponse.class);
    }

    default ResultActions updateAGenre(final GenreID id, final UpdateGenreRequest body) throws Exception {
        return this.update("/genres/", id, body);
    }
    
    default ResultActions deleteAGenre(final GenreID id) throws Exception {
        return this.delete("/genres/", id);
    }
    // __________________________________________________________________________
    
    private String given(final String url, final Object body) throws Exception {
        final var request = MockMvcRequestBuilders.post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(body));

        final var actualId = this.mvc().perform(request)
            .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse()
            .getHeader("Location").replace("%s/".formatted(url), "");

        return actualId;
    }
    
    private ResultActions givenResult(final String url, final Object body) throws Exception {
        final var request = MockMvcRequestBuilders.post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(body));

        return mvc().perform(request);
    }

    default <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper) {
        return actual.stream().map(mapper).toList();
    }

    private ResultActions list(final String url, final int page, final int perPage,
        final String search, final String sort, final String direction) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get(url)
            .queryParam("page", String.valueOf(page))
            .queryParam("perPage", String.valueOf(perPage)).queryParam("search", search)
            .queryParam("sort", sort).queryParam("dir", direction)
            .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private <T> T retrieve(final String url, final Identifier id, final Class<T> clazz) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get(url + id.getValue())
            .accept(MediaType.APPLICATION_JSON)
            //.accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON);
            //.contentType(MediaType.APPLICATION_JSON_UTF8);

        final var json = this.mvc().perform(aRequest)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        return Json.readValue(json, clazz);
    }
    
    private ResultActions retrieveResult(final String url, final Identifier id) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get(url + id.getValue())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private ResultActions delete(final String url, final Identifier id) throws Exception {
        final var aRequest = MockMvcRequestBuilders.delete(url + id.getValue()).contentType(MediaType.APPLICATION_JSON);
        return this.mvc().perform(aRequest);
    }

    private ResultActions update(final String url, final Identifier id, final Object body) throws Exception {
        final var request = MockMvcRequestBuilders.put(url + id.getValue())
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(body));

        return this.mvc().perform(request);
    }
    //_________________________________________________________________________-
}
