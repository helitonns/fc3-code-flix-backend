package com.fullcycle.admin.catalogo.application.genre.delete;

import java.util.Objects;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreID;

public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase {

    private GenreGateway genreGateway;

    //__________________________________________________________________________
    public DefaultDeleteGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(final String id) {
        this.genreGateway.deleteById(GenreID.from(id));
    }


}