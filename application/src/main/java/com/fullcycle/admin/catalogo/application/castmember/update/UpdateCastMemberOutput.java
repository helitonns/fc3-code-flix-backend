package com.fullcycle.admin.catalogo.application.castmember.update;

import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;

public record UpdateCastMemberOutput(String id) {
    
    public static UpdateCastMemberOutput from(final CastMemberID id){
        return new UpdateCastMemberOutput(id.getValue());
    }
    
    public static UpdateCastMemberOutput from(final CastMember member){
        return from(member.getId());
    }
}