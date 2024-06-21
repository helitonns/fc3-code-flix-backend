package com.fullcycle.admin.catalogo.infrastructure.entity.castmember.models;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;

public record CastMemberResponse(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("type") CastMemberType type,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt
) {
    public static CastMemberResponse with(final String id, final String name, final CastMemberType type, final Instant createdAt, final Instant updateAt){
        return new CastMemberResponse(id, name, type, createdAt, updateAt);
    }
}