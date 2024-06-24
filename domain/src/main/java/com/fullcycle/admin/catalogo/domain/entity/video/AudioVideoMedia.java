package com.fullcycle.admin.catalogo.domain.entity.video;

import java.util.Objects;
import com.fullcycle.admin.catalogo.domain.ValueObject;

public class AudioVideoMedia extends ValueObject {

    private final String checksun;
    private final String name;
    private final String rawLocation;
    private final String encodedLocation;
    private final MediaStatus status;
    
    private AudioVideoMedia(
        final String checksun, 
        final String name, 
        final String rawLocation, 
        final String encodedLocation, 
        final MediaStatus status
    ) {
        this.checksun = Objects.requireNonNull(checksun);
        this.name = Objects.requireNonNull(name);
        this.rawLocation = Objects.requireNonNull(rawLocation);
        this.encodedLocation = Objects.requireNonNull(encodedLocation);
        this.status = Objects.requireNonNull(status);
    }

    public static AudioVideoMedia with(
        final String checksun, 
        final String name, 
        final String rawLocation, 
        final String encodedLocation, 
        final MediaStatus status
    ){
        return new AudioVideoMedia(checksun, name, rawLocation, encodedLocation, status);
    }

    public String checksun() {
        return checksun;
    }

    public String name() {
        return name;
    }

    public String rawLocation() {
        return rawLocation;
    }

    public String encodedLocation() {
        return encodedLocation;
    }

    public MediaStatus status() {
        return status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((checksun == null) ? 0 : checksun.hashCode());
        result = prime * result + ((rawLocation == null) ? 0 : rawLocation.hashCode());
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
        AudioVideoMedia other = (AudioVideoMedia) obj;
        if (checksun == null) {
            if (other.checksun != null)
                return false;
        } else if (!checksun.equals(other.checksun))
            return false;
        if (rawLocation == null) {
            if (other.rawLocation != null)
                return false;
        } else if (!rawLocation.equals(other.rawLocation))
            return false;
        return true;
    }
    
    

    
}