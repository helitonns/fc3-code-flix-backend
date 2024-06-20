package com.fullcycle.admin.catalogo.infrastructure.entity.castmember.models;

import java.time.Instant;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;

public record CastMemberListResponse(
    String id,
    String name,
    CastMemberType type,
    Instant createdAt
) {
}