package com.fullcycle.admin.catalogo.application.castmember.retrieve.get;

import java.util.Objects;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;

public class DefaultGetCastMemberByIdUseCase extends GetCastMemberByIdUseCase {

    private CastMemberGateway castMemberGatway;

    
    public DefaultGetCastMemberByIdUseCase(CastMemberGateway castMemberGatway) {
        this.castMemberGatway = Objects.requireNonNull(castMemberGatway);
    }

    @Override
    public CastMemberOutput execute(final String id) {
        final var idCM = CastMemberID.from(id);
        return this.castMemberGatway.findById(idCM)
        .map(CastMemberOutput::from)
        .orElseThrow(()-> NotFoundException.with(CastMember.class, idCM));
    }
}