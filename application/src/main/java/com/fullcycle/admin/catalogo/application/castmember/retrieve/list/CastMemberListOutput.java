package com.fullcycle.admin.catalogo.application.castmember.retrieve.list;

import java.time.Instant;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;

public record CastMemberListOutput(
    String id,
    String name,
    CastMemberType type,
    Instant createdAt
) {
    public static CastMemberListOutput from(final CastMember member){
        return new CastMemberListOutput(
            member.getId().getValue(), 
            member.getName(), 
            member.getType(), 
            member.getCreatedAt()
        );
    }
}