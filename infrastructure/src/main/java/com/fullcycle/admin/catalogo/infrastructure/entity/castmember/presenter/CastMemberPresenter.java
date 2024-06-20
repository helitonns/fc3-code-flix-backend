package com.fullcycle.admin.catalogo.infrastructure.entity.castmember.presenter;

import com.fullcycle.admin.catalogo.application.castmember.retrieve.get.CastMemberOutput;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.list.CastMemberListOutput;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.models.CastMemberListResponse;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.models.CastMemberResponse;

public interface CastMemberPresenter {

    public static CastMemberResponse present(final CastMemberOutput member){
        return new CastMemberResponse(
            member.id(),
            member.name(), 
            member.type(), 
            member.createdAt(), 
            member.updatedAt()
        );
    }
    
    public static CastMemberListResponse present(final CastMemberListOutput member){
        return new CastMemberListResponse(
            member.id(),
            member.name(), 
            member.type(), 
            member.createdAt()
        );
    }
}