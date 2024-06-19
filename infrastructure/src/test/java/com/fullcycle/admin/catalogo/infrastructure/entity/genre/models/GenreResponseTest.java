package com.fullcycle.admin.catalogo.infrastructure.entity.genre.models;

import java.time.Instant;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import com.fullcycle.admin.catalogo.JacksonTest;

@JacksonTest
public class GenreResponseTest {

    @Autowired
    private JacksonTester<GenreResponse> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Acao";
        final var expectedCategories = List.of("12");
        final var expectedIsActive = true;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdateAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var response = new GenreResponse(
                expectedId,
                expectedName,
                expectedCategories,
                expectedIsActive,
                expectedCreatedAt,
                expectedUpdateAt,
                expectedDeletedAt
        );

        final var actualJson = this.json.write(response);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.categories_id", expectedCategories)
                .hasJsonPathValue("$.is_active", expectedIsActive)
                .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
                .hasJsonPathValue("$.updated_at", expectedUpdateAt.toString())
                .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString());
    }

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Acao";
        final var expectedCategory = "456";
        final var expectedIsActive = true;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdateAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var json = """
        {
            "id": "%s",
            "name": "%s",
            "categories_id": ["%s"],
            "is_active": %s,
            "created_at": "%s",
            "updated_at": "%s",
            "deleted_at": "%s"
        }
        """.formatted(
                expectedId,
                expectedName,
                expectedCategory,
                expectedIsActive,
                expectedCreatedAt.toString(),
                expectedUpdateAt.toString(),
                expectedDeletedAt.toString()
        );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("categories", List.of(expectedCategory))
                .hasFieldOrPropertyWithValue("active", expectedIsActive)
                .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
                .hasFieldOrPropertyWithValue("updatedAt", expectedUpdateAt)
                .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt);
    }
}