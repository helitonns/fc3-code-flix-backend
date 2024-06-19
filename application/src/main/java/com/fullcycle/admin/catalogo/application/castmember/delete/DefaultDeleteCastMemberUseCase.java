package com.fullcycle.admin.catalogo.application.castmember.delete;

import java.util.Objects;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;

public class DefaultDeleteCastMemberUseCase extends DeleteCastMemberUseCase {
    
    private final CastMemberGateway castMemberGatway;

    public DefaultDeleteCastMemberUseCase(final CastMemberGateway castMemberGatway) {
        this.castMemberGatway = Objects.requireNonNull(castMemberGatway);
    }

    @Override
    public void execute(String id) {
        this.castMemberGatway.deleteById(CastMemberID.from(id));
    }
}