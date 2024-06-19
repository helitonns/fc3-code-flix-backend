package com.fullcycle.admin.catalogo.application.castmember.retrieve.list;

import java.util.Objects;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;

public class DefaultListCastMembersUseCase extends ListCastMembersUseCase {

    private CastMemberGateway castMemberGatway;

    public DefaultListCastMembersUseCase(final CastMemberGateway castMemberGatway) {
        this.castMemberGatway = Objects.requireNonNull(castMemberGatway);
    }

    @Override
    public Pagination<CastMemberListOutput> execute(SearchQuery query) {
        return this.castMemberGatway.findAll(query).map(CastMemberListOutput::from);
    }
}