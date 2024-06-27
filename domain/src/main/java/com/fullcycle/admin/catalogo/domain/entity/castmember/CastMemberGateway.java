package com.fullcycle.admin.catalogo.domain.entity.castmember;

import java.util.List;
import java.util.Optional;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;

public interface CastMemberGateway {
    
    CastMember create(CastMember member);
    
    CastMember update(CastMember member);

    void deleteById(CastMemberID id);

    Optional<CastMember> findById(CastMemberID id);

    Pagination<CastMember> findAll(SearchQuery query);

    List<CastMemberID> existsByIds(Iterable<CastMemberID> ids);
}