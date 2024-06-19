package com.fullcycle.admin.catalogo.application.castmember.update;

import java.util.Objects;
import java.util.function.Supplier;
import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;

public class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase{

    private final CastMemberGateway gatway;

    public DefaultUpdateCastMemberUseCase(CastMemberGateway gatway) {
        this.gatway = Objects.requireNonNull(gatway);
    }

    @Override
    public UpdateCastMemberOutput execute(final UpdateCastMemberCommand command) {
        final var id = CastMemberID.from(command.id());
        
        final var member = this.gatway.findById(id).orElseThrow(notFound(id));
        final var notification = Notification.create();
        notification.validate(()-> member.update(command.name(), command.type()));

        if(notification.hasError()){
            notify(id, notification);
        }   

        return UpdateCastMemberOutput.from(this.gatway.update(member));
    }

    private Supplier<NotFoundException> notFound(CastMemberID id){
        return ()-> NotFoundException.with(CastMember.class, id);
    }

    private void notify(final Identifier id, final Notification notification){
        throw new NotificationException("Could not update Aggregate CastMember %s".formatted(id.getValue()), notification);
    }
}