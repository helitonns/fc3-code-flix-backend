package com.fullcycle.admin.catalogo.domain.entity.video;

public interface MediaResourceGateway {

    AudioVideoMedia storeAudioVideo(VideoID id, Resource resource);
    
    ImageMedia storeImage(VideoID id, Resource resource);
    
    void clearResource(VideoID id);
}