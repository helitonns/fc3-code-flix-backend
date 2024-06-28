package com.fullcycle.admin.catalogo.infrastructure.entity.castmember;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.persistence.CastMemberRepository;
import com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils;

@Component
public class CastMemberMySQLGateway implements CastMemberGateway {

    private final CastMemberRepository repository;

    public CastMemberMySQLGateway(final CastMemberRepository castMemberRepository) {
        this.repository = Objects.requireNonNull(castMemberRepository);
    }

    @Override
    public CastMember create(final CastMember member) {
        return save(member);
    }

    @Override
    public CastMember update(final CastMember member) {
        return save(member);
    }

    @Override
    public void deleteById(final CastMemberID id) {
        final var idCM = id.getValue();

        if(this.repository.existsById(idCM)){
            this.repository.deleteById(idCM);
        }
    }

    @Override
    public Optional<CastMember> findById(final CastMemberID id) {
        return this.repository.findById(id.getValue())
            .map(CastMemberJpaEntity::toAggregate);
    }

    @Override
    public Pagination<CastMember> findAll(final SearchQuery query) {
        final var page = PageRequest.of(
            query.page(), 
            query.perPage(), 
            Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var where = Optional.ofNullable(query.terms())
            .filter(str -> !str.isBlank())
            .map(this::assembleSpecification)
            .orElse(null);
        
        final var pageResult = this.repository.findAll(where, page);

        return new Pagination<>(
            pageResult.getNumber(), 
            pageResult.getSize(), 
            pageResult.getTotalElements(), 
            pageResult.map(CastMemberJpaEntity::toAggregate).toList()
        );
    }

    //__________________________________________________________________________
    private CastMember save(final CastMember member) {
        return this.repository.save(CastMemberJpaEntity.from(member)).toAggregate();
    }

    private Specification<CastMemberJpaEntity> assembleSpecification(final String terms){
        return SpecificationUtils.like("name", terms);
    }

    @Override
    public List<CastMemberID> existsByIds(Iterable<CastMemberID> ids) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByIds'");
    }

}