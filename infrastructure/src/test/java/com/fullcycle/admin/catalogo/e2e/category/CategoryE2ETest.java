package com.fullcycle.admin.catalogo.e2e.category;

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
import com.fullcycle.admin.catalogo.e2e.MockDsl;
import com.fullcycle.admin.catalogo.infrastructure.entity.category.models.UpdateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.entity.category.persistence.CategoryRepository;

@E2ETest
@Testcontainers
public class CategoryE2ETest implements MockDsl {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private CategoryRepository categoryRepository;

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
        public void asACatalogAdminIShouldBeAbleToCreateNewCategoryWithValidValues() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, categoryRepository.count());

                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedDIsActive = true;

                final var actualId = givenAValidCategoryID(expectedName, expectedDescription, expectedDIsActive);

                final var actualCategory = retrieveACetegory(actualId);

                Assertions.assertEquals(expectedName, actualCategory.name());
                Assertions.assertEquals(expectedDescription, actualCategory.description());
                Assertions.assertEquals(expectedDIsActive, actualCategory.active());
                Assertions.assertNotNull(actualCategory.createdAt());
                Assertions.assertNotNull(actualCategory.updatedAt());
                Assertions.assertNull(actualCategory.deletedAt());
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToNavigateToAllCategories() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, categoryRepository.count());

                givenAValidCategoryID("Filmes", null, true);
                givenAValidCategoryID("Documentários", null, true);
                givenAValidCategoryID("Séries", null, true);

                listCategories(0, 1).andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.current_page",Matchers.equalTo(0)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Documentários")));

                listCategories(1, 1).andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(1)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Filmes")));

                listCategories(2, 1).andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(2)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Séries")));

                listCategories(3, 1).andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(3)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToSearchBetweenAllCategories() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, categoryRepository.count());

                givenAValidCategoryID("Filmes", null, true);
                givenAValidCategoryID("Documentários", null, true);
                givenAValidCategoryID("Séries", null, true);

                listCategories(0, 1, "fil").andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(1)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Filmes")));
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToSortAllCategoriesByDescriptionDesc() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, categoryRepository.count());

                givenAValidCategoryID("Filmes", "C", true);
                givenAValidCategoryID("Documentários", "Z", true);
                givenAValidCategoryID("Séries", "A", true);

                listCategories(0, 3, "", "description", "desc")
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.current_page",Matchers.equalTo(0)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.per_page",Matchers.equalTo(3)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.total",Matchers.equalTo(3)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.items",Matchers.hasSize(3)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Documentários")))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name", Matchers.equalTo("Filmes")))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name", Matchers.equalTo("Séries")));
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToGetCategoryByItsIdentifier() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, categoryRepository.count());

                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedDIsActive = true;

                final var actualId = givenAValidCategoryID(expectedName, expectedDescription,
                                expectedDIsActive);

                final var actualCategory = retrieveACetegory(actualId);

                Assertions.assertEquals(expectedName, actualCategory.name());
                Assertions.assertEquals(expectedDescription, actualCategory.description());
                Assertions.assertEquals(expectedDIsActive, actualCategory.active());
                Assertions.assertNotNull(actualCategory.createdAt());
                Assertions.assertNotNull(actualCategory.updatedAt());
                Assertions.assertNull(actualCategory.deletedAt());
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategory() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, categoryRepository.count());

                final var aRequest = MockMvcRequestBuilders.get("/categories/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

                this.mvc.perform(aRequest).andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers
                        .equalTo("Category with ID 123 was not found")));
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToUpdateACategoryByItsIdentifier() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, categoryRepository.count());

                final var actualId = givenAValidCategoryID("Movies", null, true);

                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedDIsActive = true;

                final var requestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedDIsActive);

                updateACategory(actualId, requestBody).andExpect(MockMvcResultMatchers.status().isOk());

                final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

                Assertions.assertEquals(expectedName, actualCategory.getName());
                Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
                Assertions.assertEquals(expectedDIsActive, actualCategory.isActive());
                Assertions.assertNotNull(actualCategory.getCreatedAt());
                Assertions.assertNotNull(actualCategory.getUpdatedAt());
                Assertions.assertNull(actualCategory.getDeletedAt());
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToInactivateACategoryByItsIdentifier() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, categoryRepository.count());

                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedDIsActive = false;

                final var actualId = givenAValidCategoryID(expectedName, expectedDescription, true);

                final var requestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedDIsActive);

                updateACategory(actualId, requestBody).andExpect(MockMvcResultMatchers.status().isOk());

                final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

                Assertions.assertEquals(expectedName, actualCategory.getName());
                Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
                Assertions.assertEquals(expectedDIsActive, actualCategory.isActive());
                Assertions.assertNotNull(actualCategory.getCreatedAt());
                Assertions.assertNotNull(actualCategory.getUpdatedAt());
                Assertions.assertNotNull(actualCategory.getDeletedAt());
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToActivateACategoryByItsIdentifier() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, categoryRepository.count());

                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedDIsActive = true;

                final var actualId = givenAValidCategoryID(expectedName, expectedDescription, false);

                final var requestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedDIsActive);

                updateACategory(actualId, requestBody).andExpect(MockMvcResultMatchers.status().isOk());

                final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

                Assertions.assertEquals(expectedName, actualCategory.getName());
                Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
                Assertions.assertEquals(expectedDIsActive, actualCategory.isActive());
                Assertions.assertNotNull(actualCategory.getCreatedAt());
                Assertions.assertNotNull(actualCategory.getUpdatedAt());
                Assertions.assertNull(actualCategory.getDeletedAt());
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToDeleteCategoryByItsIdentifier() throws Exception {
                Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
                Assertions.assertEquals(0, categoryRepository.count());

                final var actualId = givenAValidCategoryID("Filmes", null, true);

                deleteACategory(actualId).andExpect(MockMvcResultMatchers.status().isNoContent());

                Assertions.assertFalse(this.categoryRepository.existsById(actualId.getValue()));
        }

}


