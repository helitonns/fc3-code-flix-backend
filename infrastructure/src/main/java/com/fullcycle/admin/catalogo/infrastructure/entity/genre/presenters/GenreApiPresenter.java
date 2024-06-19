package com.fullcycle.admin.catalogo.infrastructure.entity.genre.presenters;

import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GenreOutput;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.GenreListOutput;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.models.GenreListResponse;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.models.GenreResponse;

public interface GenreApiPresenter {
    static GenreResponse present(final GenreOutput output){
        return new GenreResponse(
                output.id(),
                output.name(),
                output.categories(),
                output.active(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static GenreListResponse present(final GenreListOutput output){
        return new GenreListResponse(
                output.id(),
                output.name(),
                output.active(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}