package com.fullcycle.admin.catalogo.infrastructure.entity.genre;

import com.fullcycle.admin.catalogo.domain.entity.genre.Genre;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.persistence.GenreJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.persistence.GenreRepository;
import com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository genreRepository;

    //__________________________________________________________________________
    public GenreMySQLGateway(final GenreRepository genreRepository) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
    }
    
    @Override
    public Genre create(final Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public Genre update(final Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public void deleteById(final GenreID anId) {
        final var aGenreId = anId.getValue();
        if (this.genreRepository.existsById(aGenreId)) {
            this.genreRepository.deleteById(aGenreId);
        }
    }

    @Override
    public Optional<Genre> findById(final GenreID anId) {
        return this.genreRepository.findById(anId.getValue()).map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Genre> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
            aQuery.page(), 
            aQuery.perPage(),
            Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var where = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult = this.genreRepository.findAll(Specification.where(where), page);

        return new Pagination<>(
            pageResult.getNumber(), 
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.map(GenreJpaEntity::toAggregate).toList()
        );
    }

    private Genre save(final Genre aGenre) {
        return this.genreRepository.save(GenreJpaEntity.from(aGenre)).toAggregate();
    }

    private Specification<GenreJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }

    @Override
    public List<GenreID> existsByIds(Iterable<GenreID> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByIds'");
    }
}
