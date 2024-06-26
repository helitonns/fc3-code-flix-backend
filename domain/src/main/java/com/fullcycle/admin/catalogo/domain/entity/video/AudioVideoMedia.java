package com.fullcycle.admin.catalogo.domain.entity.video;

import java.util.Objects;
import com.fullcycle.admin.catalogo.domain.ValueObject;

public class AudioVideoMedia extends ValueObject {

    private final String checksum;
    private final String name;
    private final String rawLocation;
    private final String encodedLocation;
    private final MediaStatus status;
    
    private AudioVideoMedia(
        final String checksum, 
        final String name, 
        final String rawLocation, 
        final String encodedLocation, 
        final MediaStatus status
    ) {
        this.checksum = Objects.requireNonNull(checksum);
        this.name = Objects.requireNonNull(name);
        this.rawLocation = Objects.requireNonNull(rawLocation);
        this.encodedLocation = Objects.requireNonNull(encodedLocation);
        this.status = Objects.requireNonNull(status);
    }

    public static AudioVideoMedia with(
        final String checksum, 
        final String name, 
        final String rawLocation, 
        final String encodedLocation, 
        final MediaStatus status
    ){
        return new AudioVideoMedia(checksum, name, rawLocation, encodedLocation, status);
    }

    public String checksum() {
        return checksum;
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
        result = prime * result + ((checksum == null) ? 0 : checksum.hashCode());
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
        if (checksum == null) {
            if (other.checksum != null)
                return false;
        } else if (!checksum.equals(other.checksum))
            return false;
        if (rawLocation == null) {
            if (other.rawLocation != null)
                return false;
        } else if (!rawLocation.equals(other.rawLocation))
            return false;
        return true;
    }
    
    

    
}