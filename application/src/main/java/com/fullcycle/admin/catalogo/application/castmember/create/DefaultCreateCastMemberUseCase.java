package com.fullcycle.admin.catalogo.application.castmember.create;

import java.util.Objects;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;

public class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase {

    private final CastMemberGateway castMemberGatway;

    public DefaultCreateCastMemberUseCase(CastMemberGateway castMemberGatway) {
        this.castMemberGatway = Objects.requireNonNull(castMemberGatway);
    }
    //__________________________________________________________________________


    @Override
    public CreateCastMemberOutput execute(CreateCastMemberCommand command) {
        final var notification = Notification.create();
        final var member = notification.validate(() -> CastMember.newMember(command.name(), command.type()));

        if(notification.hasError()){
            throw new NotificationException("Could not create Aggregate CastMember", notification);
        }

        return CreateCastMemberOutput.from(castMemberGatway.create(member));
    }

}
