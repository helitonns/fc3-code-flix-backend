package com.fullcycle.admin.catalogo.application.castmember.retrieve.get;

import java.time.Instant;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;

public record CastMemberOutput(
    String id,
    String name,
    CastMemberType type,
    Instant createdAt,
    Instant updatedAt
) {
    public static CastMemberOutput from(final CastMember member){
        return new CastMemberOutput(member.getId().getValue(), member.getName(), member.getType(), member.getCreatedAt(), member.getUpdatedAt());
    }

}