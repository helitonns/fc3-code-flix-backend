package com.fullcycle.admin.catalogo.e2e.castmember;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fullcycle.admin.catalogo.E2ETest;
import com.fullcycle.admin.catalogo.Fixture;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.e2e.MockDsl;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.persistence.CastMemberRepository;

@E2ETest
@Testcontainers
public class CastMemberE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CastMemberRepository repository;

    @SuppressWarnings({"rawtypes", "resource"})
    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:8.0")
        .withPassword("123456")
        .withUsername("root")
        .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Override
    public MockMvc mvc() {
        return mvc;
    }
    //__________________________________________________________________________

    @Test
    public void asCatalogAdminIShouldBeAbleToCreateANewCastMemberWithValidValues() throws Exception{
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var actualMemberId = givenACastMember(expectedName, expectedType);

        final var actualMember = repository.findById(actualMemberId.getValue()).get();

        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
        Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToSeeTreatedErrorByCreatingANewCastMemberWithInvalidValues() throws Exception{
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "'name' should not be null";

        givenACastMemberResult(expectedName, expectedType)
            .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
            .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToNavigateThruAllMembers() throws Exception{
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenACastMember("Jason Momoa", CastMemberType.ACTOR);

        listCastMembers(0, 1)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Jason Momoa")));
        
        listCastMembers(1, 1)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Quentin Tarantino")));
        
        listCastMembers(2, 1)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Vin Diesel")));
        
        listCastMembers(3, 1)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToSearchThruAllMembers() throws Exception{
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenACastMember("Jason Momoa", CastMemberType.ACTOR);

        listCastMembers(0, 1, "vin")
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Vin Diesel")));
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToSortAllMembersByNameDesc() throws Exception{
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenACastMember("Jason Momoa", CastMemberType.ACTOR);

        listCastMembers(0, 3, "", "name", "desc")
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(3)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Vin Diesel")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name", Matchers.equalTo("Quentin Tarantino")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name", Matchers.equalTo("Jason Momoa")));
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToGetCastMemberByItsIdentifier() throws Exception{
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        givenACastMember(Fixture.name(), Fixture.CastMember.type());
        givenACastMember(Fixture.name(), Fixture.CastMember.type());
        final var actualId = givenACastMember(expectedName, expectedType);

        final var actualMember = retrieveACastMember(actualId);

        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertNotNull(actualMember.createdAt());
        Assertions.assertNotNull(actualMember.updatedAt());
        Assertions.assertEquals(actualMember.createdAt(), actualMember.updatedAt());
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCastMember() throws Exception{
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        givenACastMember(Fixture.name(), Fixture.CastMember.type());
        givenACastMember(Fixture.name(), Fixture.CastMember.type());

        retrieveACastMemberResult(CastMemberID.from("123"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo("CastMember with ID 123 was not found")));
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToUpdateCastMemberByItsIdentifier() throws Exception{
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        givenACastMember(Fixture.name(), Fixture.CastMember.type());
        final var actualId = givenACastMember("vin", CastMemberType.ACTOR);

        updateACastMember(actualId, expectedName, expectedType).andExpect(MockMvcResultMatchers.status().isOk());

        final var actualMember = retrieveACastMember(actualId);

        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertNotNull(actualMember.createdAt());
        Assertions.assertNotNull(actualMember.updatedAt());
    }
    
    @Test
    public void asCatalogAdminIShouldBeAbleToSeeATreateErrordByUpdatingCastMemberWithInvalidValue() throws Exception{
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be null";

        givenACastMember(Fixture.name(), Fixture.CastMember.type());
        final var actualId = givenACastMember("vin", CastMemberType.ACTOR);

        updateACastMember(actualId, expectedName, expectedType)
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));
    }
    
    @Test
    public void asCatalogAdminIShouldBeAbleToDeleteCastMemberByItsIdentifier() throws Exception{
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        givenACastMember(Fixture.name(), Fixture.CastMember.type());
        final var actualId = givenACastMember(Fixture.name(), Fixture.CastMember.type());

        Assertions.assertEquals(2, repository.count());

        deleteACastMember(actualId).andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertEquals(1, repository.count());
        Assertions.assertFalse(repository.existsById(actualId.getValue()));
    }
    
    @Test
    public void asCatalogAdminIShouldBeAbleToNotSeeAErrorByDeletingCastMemberByItsIdentifier() throws Exception{
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        givenACastMember(Fixture.name(), Fixture.CastMember.type());
        givenACastMember(Fixture.name(), Fixture.CastMember.type());

        Assertions.assertEquals(2, repository.count());

        deleteACastMember(CastMemberID.from("123")).andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertEquals(2, repository.count());
    }



}