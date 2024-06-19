package com.fullcycle.admin.catalogo.domain.entity.castmember;

import java.time.Instant;
import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;

public class CastMember extends AggregateRoot<CastMemberID> {

    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;
    
    protected CastMember(final CastMemberID id, final String name, final CastMemberType type, final Instant createdAt, final Instant updatedAt) {
        super(id);
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        selfValidate();
    }

    public static CastMember newMember(final String name, final CastMemberType type){
        final var id = CastMemberID.unique();
        final var now = Instant.now();

        return new CastMember(id, name, type, now, now);
    }

    public static CastMember with(final CastMemberID id, final String name, final CastMemberType type, final Instant createdAt, final Instant updatedAt){
        return new CastMember(id, name, type, createdAt, updatedAt);
    }
    
    public static CastMember with(final CastMember member){
        return new CastMember(member.getId(), member.getName(), member.getType(), member.getCreatedAt(), member.getUpdatedAt());
    }

    public CastMember update(final String name, final CastMemberType type){
        this.name = name;
        this.type = type;
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);
        
        if (notification.hasError()) {
            throw new NotificationException("Falid to create a Aggregate CastMember", notification);
        }
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CastMemberValidator(this, handler).validate();;
    }

    public String getName() {
        return name;
    }

    public CastMemberType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

}