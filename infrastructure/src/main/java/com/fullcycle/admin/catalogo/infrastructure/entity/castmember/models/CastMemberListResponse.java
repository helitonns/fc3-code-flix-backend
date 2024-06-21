package com.fullcycle.admin.catalogo.infrastructure.entity.castmember.models;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;

public record CastMemberListResponse(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("type") CastMemberType type,
    @JsonProperty("crated_at") Instant createdAt
) {
}