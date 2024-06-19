package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreCommand;
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreCommand;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.api.GenreAPI;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.models.CreateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.models.GenreListResponse;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.models.GenreResponse;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.models.UpdateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.presenters.GenreApiPresenter;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase; 
    private final GetGenreByIdUseCase getGenreByIdUseCase; 
    private final UpdateGenreUseCase updateGenreUseCase; 
    private final DeleteGenreUseCase deleteGenreUseCase; 
    private final ListGenreUseCase listGenreUseCase; 

    public GenreController(
        final CreateGenreUseCase createGenreUseCase,
        final GetGenreByIdUseCase getGenreByIdUseCase,
        final UpdateGenreUseCase updateGenreUseCase,
        final DeleteGenreUseCase deleteGenreUseCase,
        final ListGenreUseCase listGenreUseCase
    ) {
        this.createGenreUseCase = createGenreUseCase;
        this.getGenreByIdUseCase = getGenreByIdUseCase;
        this.updateGenreUseCase = updateGenreUseCase;
        this.deleteGenreUseCase = deleteGenreUseCase;
        this.listGenreUseCase = listGenreUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        final var command = CreateGenreCommand.with(
            input.name(),
            input.isActive(),
            input.categories()
        );

        final var output = this.createGenreUseCase.execute(command);
        
        return ResponseEntity.created(URI.create("/genres/"+output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(final String search, final int page, final int perPage, final String sort, final String direction) {
        return this.listGenreUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
            .map(GenreApiPresenter::present);
    }

    @Override
    public GenreResponse getById(final String id) {
        return GenreApiPresenter.present(this.getGenreByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateGenreRequest input) {
        final var command = UpdateGenreCommand.with(
            id,
            input.name(),
            input.isActive(),
            input.categories()
        );
        
        final var output = this.updateGenreUseCase.execute(command);
        
        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteGenreUseCase.execute(id);
    }
}