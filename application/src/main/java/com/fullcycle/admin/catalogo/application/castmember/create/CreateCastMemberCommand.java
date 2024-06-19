package com.fullcycle.admin.catalogo.application.castmember.create;

import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;

public record CreateCastMemberCommand(
    String name,
    CastMemberType type
) {
    public static CreateCastMemberCommand with(final String name, final CastMemberType type){
        return new CreateCastMemberCommand(name, type);
    }
}