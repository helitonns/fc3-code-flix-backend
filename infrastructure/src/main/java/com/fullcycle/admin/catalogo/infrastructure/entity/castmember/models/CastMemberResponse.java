package com.fullcycle.admin.catalogo.infrastructure.entity.castmember.models;

import java.time.Instant;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;

public record CastMemberResponse(
    String id,
    String name,
    CastMemberType type,
    Instant createdAt,
    Instant updatedAt
) {
    public static CastMemberResponse with(final String id, final String name, final CastMemberType type, final Instant createdAt, final Instant updateAt){
        return new CastMemberResponse(id, name, type, createdAt, updateAt);
    }
}