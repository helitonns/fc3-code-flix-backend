package com.fullcycle.admin.catalogo.e2e.genre;

import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fullcycle.admin.catalogo.E2ETest;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.e2e.MockDsl;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.models.UpdateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.persistence.GenreRepository;

@E2ETest
@Testcontainers
public class GenreE2ETest implements MockDsl {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private GenreRepository genreRepository;

        @SuppressWarnings({"rawtypes", "resource"})
        @Container
        private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:8.0")
                        .withPassword("123456").withUsername("root").withDatabaseName("adm_videos");

        @DynamicPropertySource
        public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
                registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
        }

        @Override
        public MockMvc mvc() {
                return this.mvc;
        }

        @Test
        public void asCatalogAdminIShouldBeAbleToCreateANewGenreWithValidValues() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, genreRepository.count());

                final var expectedName = "Acao";
                final var expectedIsActive = true;
                final var expectedCategories = List.<CategoryID>of();

                final var actualId =
                                givenAGenre(expectedName, expectedCategories, expectedIsActive);

                final var actualGenre = genreRepository.findById(actualId.getValue()).get();

                Assertions.assertEquals(expectedName, actualGenre.getName());
                Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
                Assertions.assertTrue(
                                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                                                && expectedCategories.containsAll(
                                                                actualGenre.getCategoryIDs()));
                Assertions.assertNotNull(actualGenre.getCreatedAt());
                Assertions.assertNotNull(actualGenre.getUpdatedAt());
                Assertions.assertNull(actualGenre.getDeletedAt());
        }

        @Test
        public void asCatalogAdminIShouldBeAbleToCreateANewGenreWithCategories() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, genreRepository.count());

                final var filmes = givenAValidCategoryID("Filmes", null, true);

                final var expectedName = "Acao";
                final var expectedIsActive = true;
                final var expectedCategories = List.of(filmes);

                final var actualId =
                                givenAGenre(expectedName, expectedCategories, expectedIsActive);

                final var actualGenre = genreRepository.findById(actualId.getValue()).get();

                Assertions.assertEquals(expectedName, actualGenre.getName());
                Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
                Assertions.assertTrue(
                                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                                                && expectedCategories.containsAll(
                                                                actualGenre.getCategoryIDs()));
                Assertions.assertNotNull(actualGenre.getCreatedAt());
                Assertions.assertNotNull(actualGenre.getUpdatedAt());
                Assertions.assertNull(actualGenre.getDeletedAt());
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToNavigateThruAllGenres() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, genreRepository.count());

                givenAGenre("Acao", List.of(), true);
                givenAGenre("Esportes", List.of(), true);
                givenAGenre("Drama", List.of(), true);

                listGenres(0, 1).andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page",
                                                Matchers.equalTo(0)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page",
                                                Matchers.equalTo(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.total",
                                                Matchers.equalTo(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items",
                                                Matchers.hasSize(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name",
                                                Matchers.equalTo("Acao")));

                listGenres(1, 1).andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page",
                                                Matchers.equalTo(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page",
                                                Matchers.equalTo(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.total",
                                                Matchers.equalTo(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items",
                                                Matchers.hasSize(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name",
                                                Matchers.equalTo("Drama")));

                listGenres(2, 1).andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page",
                                                Matchers.equalTo(2)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page",
                                                Matchers.equalTo(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.total",
                                                Matchers.equalTo(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items",
                                                Matchers.hasSize(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name",
                                                Matchers.equalTo("Esportes")));

                listGenres(3, 1).andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page",
                                                Matchers.equalTo(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page",
                                                Matchers.equalTo(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.total",
                                                Matchers.equalTo(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items",
                                                Matchers.hasSize(0)));
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToSearchBetweenAllGenres() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, genreRepository.count());

                givenAGenre("Acao", List.of(), true);
                givenAGenre("Esportes", List.of(), true);
                givenAGenre("Drama", List.of(), true);

                listGenres(0, 1, "dra").andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page",
                                                Matchers.equalTo(0)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page",
                                                Matchers.equalTo(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.total",
                                                Matchers.equalTo(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items",
                                                Matchers.hasSize(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name",
                                                Matchers.equalTo("Drama")));
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToSortAllGenresByNameDesc() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, genreRepository.count());

                givenAGenre("Acao", List.of(), true);
                givenAGenre("Esportes", List.of(), true);
                givenAGenre("Drama", List.of(), true);

                listGenres(0, 3, "", "name", "desc")
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page",
                                                Matchers.equalTo(0)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page",
                                                Matchers.equalTo(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.total",
                                                Matchers.equalTo(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items",
                                                Matchers.hasSize(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name",
                                                Matchers.equalTo("Esportes")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name",
                                                Matchers.equalTo("Drama")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name",
                                                Matchers.equalTo("Acao")));
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToGetGenreByItsIdentifier() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, genreRepository.count());

                final var filmes = givenAValidCategoryID("Filmes", null, true);

                final var expectedName = "Acao";
                final var expectedDIsActive = true;
                final var expectedCategories = List.of(filmes);

                final var actualId =
                                givenAGenre(expectedName, expectedCategories, expectedDIsActive);

                final var actualGenre = retrieveAGenre(actualId);

                Assertions.assertEquals(expectedName, actualGenre.name());
                Assertions.assertTrue(expectedCategories.size() == actualGenre.categories().size()
                                && mapTo(expectedCategories, CategoryID::getValue)
                                                .containsAll(actualGenre.categories()));
                Assertions.assertEquals(expectedDIsActive, actualGenre.active());
                Assertions.assertNotNull(actualGenre.createdAt());
                Assertions.assertNotNull(actualGenre.updatedAt());
                Assertions.assertNull(actualGenre.deletedAt());
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundGenre() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, genreRepository.count());

                final var aRequest = MockMvcRequestBuilders.get("/genres/123")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON);

                this.mvc.perform(aRequest).andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers
                                                .equalTo("Genre with ID 123 was not found")));
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToUpdateAGenreByItsIdentifier() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, genreRepository.count());

                final var filmes = givenAValidCategoryID("Filmes", null, true);

                final var expectedName = "Acao";
                final var expectedIsActive = true;
                final var expectedCategories = List.of(filmes);
                
                final var actualId = givenAGenre("asd", expectedCategories, expectedIsActive);

                final var requestBody = new UpdateGenreRequest(
                        expectedName, 
                        mapTo(expectedCategories, 
                        CategoryID::getValue), 
                        expectedIsActive
                );

                updateAGenre(actualId, requestBody).andExpect(MockMvcResultMatchers.status().isOk());

                final var actualGenre = genreRepository.findById(actualId.getValue()).get();

                Assertions.assertEquals(expectedName, actualGenre.getName());
                Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size()
                                && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
                Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
                Assertions.assertNotNull(actualGenre.getCreatedAt());
                Assertions.assertNotNull(actualGenre.getUpdatedAt());
                Assertions.assertNull(actualGenre.getDeletedAt());
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToInactivateAGenreByItsIdentifier() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, genreRepository.count());

                final var filmes = givenAValidCategoryID("Filmes", null, true);

                final var expectedName = "Acao";
                final var expectedIsActive = false;
                final var expectedCategories = List.of(filmes);
                
                final var actualId = givenAGenre(expectedName, expectedCategories, true);

                final var requestBody = new UpdateGenreRequest(
                        expectedName, 
                        mapTo(expectedCategories, 
                        CategoryID::getValue), 
                        expectedIsActive
                );

                updateAGenre(actualId, requestBody).andExpect(MockMvcResultMatchers.status().isOk());

                final var actualGenre = genreRepository.findById(actualId.getValue()).get();

                Assertions.assertEquals(expectedName, actualGenre.getName());
                Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size()
                                && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
                Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
                Assertions.assertNotNull(actualGenre.getCreatedAt());
                Assertions.assertNotNull(actualGenre.getUpdatedAt());
                Assertions.assertNotNull(actualGenre.getDeletedAt());
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToActivateAGenreByItsIdentifier() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, genreRepository.count());

                final var filmes = givenAValidCategoryID("Filmes", null, true);

                final var expectedName = "Acao";
                final var expectedIsActive = true;
                final var expectedCategories = List.of(filmes);
                
                final var actualId = givenAGenre(expectedName, expectedCategories, false);

                final var requestBody = new UpdateGenreRequest(
                        expectedName, 
                        mapTo(expectedCategories, 
                        CategoryID::getValue), 
                        expectedIsActive
                );

                updateAGenre(actualId, requestBody).andExpect(MockMvcResultMatchers.status().isOk());

                final var actualGenre = genreRepository.findById(actualId.getValue()).get();

                Assertions.assertEquals(expectedName, actualGenre.getName());
                Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoryIDs().size()
                                && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
                Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
                Assertions.assertNotNull(actualGenre.getCreatedAt());
                Assertions.assertNotNull(actualGenre.getUpdatedAt());
                Assertions.assertNull(actualGenre.getDeletedAt());
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToDeleteGenreByItsIdentifier() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, genreRepository.count());

                final var filmes = givenAValidCategoryID("Filmes", null, true);

                final var actualId = givenAGenre("Filmes", List.of(filmes), true);

                deleteAGenre(actualId).andExpect(MockMvcResultMatchers.status().isNoContent());

                Assertions.assertFalse(this.genreRepository.existsById(actualId.getValue()));
                Assertions.assertEquals(0, genreRepository.count());
        }

}
