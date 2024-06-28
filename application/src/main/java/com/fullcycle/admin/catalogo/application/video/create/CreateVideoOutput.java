package com.fullcycle.admin.catalogo.application.video.create;

import com.fullcycle.admin.catalogo.domain.entity.video.Video;

public record CreateVideoOutput(String id) {

    public static CreateVideoOutput from(final Video video){
        return new CreateVideoOutput(video.getId().getValue());
    }
}