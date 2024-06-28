package com.fullcycle.admin.catalogo.domain.entity.video;

import java.util.Objects;
import com.fullcycle.admin.catalogo.domain.ValueObject;

public class Resource extends ValueObject {
    private final byte[] content;
    private final String contentType;
    private final String name;
    private final Type type;

    //__________________________________________________________________________
    private Resource(final byte[] content, final String contentType, final String name, final Type type) {
        this.content = Objects.requireNonNull(content);
        this.contentType = Objects.requireNonNull(contentType);
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
    }
    
    public static Resource with(final byte[] content, final String contentType, final String name, final Type type) {
        return new Resource(content, contentType, name, type);
    }

    public byte[] content() {
        return this.content;
    }


    public String contentType() {
        return this.contentType;
    }


    public String name() {
        return this.name;
    }

    public Type type() {
        return this.type;
    }

    public enum Type{
        VIDEO,
        TRAILER,
        BANNER,
        THUMBNAIL,
        THUMBNAIL_HALF
    }
}