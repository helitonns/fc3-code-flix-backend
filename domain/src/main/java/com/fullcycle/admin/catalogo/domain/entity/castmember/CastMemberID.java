package com.fullcycle.admin.catalogo.domain.entity.castmember;

import java.util.Objects;
import java.util.UUID;
import com.fullcycle.admin.catalogo.domain.Identifier;

public class CastMemberID extends Identifier{

    private final String value;

    private CastMemberID(String id) {
        this.value = Objects.requireNonNull(id);
    }

    public static CastMemberID unique() {
        return CastMemberID.from(UUID.randomUUID());
    }

    public static CastMemberID from(final String id) {
        return new CastMemberID(id);
    }

    public static CastMemberID from(final UUID id) {
        return new CastMemberID(id.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CastMemberID other = (CastMemberID) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    
}