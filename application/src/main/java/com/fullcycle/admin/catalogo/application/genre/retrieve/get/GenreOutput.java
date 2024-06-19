package com.fullcycle.admin.catalogo.application.genre.retrieve.get;

import java.time.Instant;
import java.util.List;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.entity.genre.Genre;

public record GenreOutput(
    String id,
    String name,
    boolean active,
    List<String> categories,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt
) {
    public static GenreOutput from(final Genre genre){
        return new GenreOutput(
            genre.getId().getValue(), 
            genre.getName(), 
            genre.isActive(), 
            genre.getCategories().stream().map(CategoryID::getValue).toList(), 
            genre.getCreatedAt(), 
            genre.getUpdatedAt(), 
            genre.getDeletedAt()
        );
    }
}