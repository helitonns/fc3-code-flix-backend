package com.fullcycle.admin.catalogo.application.genre.retrieve.list;

import java.time.Instant;
import java.util.List;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.entity.genre.Genre;

public record GenreListOutput(
    String id,
    String name,
    boolean active,
    List<String> categories,
    Instant createdAt,
    Instant deletedAt
) {
    public static GenreListOutput from(final Genre genre){
        return new GenreListOutput(
            genre.getId().getValue(),
            genre.getName(),
            genre.isActive(),
            genre.getCategories().stream().map(CategoryID::getValue).toList(),
            genre.getCreatedAt(),
            genre.getDeletedAt()
        );
    }
}