package com.fullcycle.admin.catalogo.application.genre.retrieve.get;

import java.util.Objects;
import com.fullcycle.admin.catalogo.domain.entity.genre.Genre;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;

public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase {

    private GenreGateway genreGateway;

    //__________________________________________________________________________
    public DefaultGetGenreByIdUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public GenreOutput execute(final String id) {
        return this.genreGateway.findById(GenreID.from(id))
        .map(GenreOutput::from)
        .orElseThrow(()-> NotFoundException.with(Genre.class, GenreID.from(id)));
    }
}