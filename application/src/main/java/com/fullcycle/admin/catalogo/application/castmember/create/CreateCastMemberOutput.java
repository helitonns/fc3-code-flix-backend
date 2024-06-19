package com.fullcycle.admin.catalogo.application.castmember.create;

import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;

public record CreateCastMemberOutput(
    String id
) {
    public static CreateCastMemberOutput from(final CastMemberID id){
        return new CreateCastMemberOutput(id.getValue());
    }

    public static CreateCastMemberOutput from(final CastMember castMember){
        return from(castMember.getId());
    }
    
    
}