package com.fullcycle.admin.catalogo.domain.entity.genre;

import java.util.List;
import java.util.Optional;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;

public interface GenreGateway {

    Genre create(Genre genre);

    void deleteById(GenreID id);

    Optional<Genre> findById(GenreID id);

    Genre update(Genre genre);

    Pagination<Genre> findAll(SearchQuery query);

    List<GenreID> existsByIds(Iterable<GenreID> ids);
}
