package com.fullcycle.admin.catalogo.application.genre.retrieve.list;

import java.util.Objects;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;

public class DefaultListGenreUseCase extends ListGenreUseCase{

    private final GenreGateway genreGateway;

    //__________________________________________________________________________
    public DefaultListGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }
    
    @Override
    public Pagination<GenreListOutput> execute(final SearchQuery query) {
        return this.genreGateway.findAll(query).map(GenreListOutput::from);
    }

    
}