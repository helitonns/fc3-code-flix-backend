package com.fullcycle.admin.catalogo.infrastructure.entity.castmember.models;

import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;

public record CreateCastMemberRequest(
    String name,
    CastMemberType type
) {
}