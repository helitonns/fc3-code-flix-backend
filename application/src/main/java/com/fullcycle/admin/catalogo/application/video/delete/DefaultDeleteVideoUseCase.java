package com.fullcycle.admin.catalogo.application.video.delete;

import java.util.Objects;
import com.fullcycle.admin.catalogo.domain.entity.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.entity.video.VideoID;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;

    public DefaultDeleteVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(final String id) {
        this.videoGateway.deletebyId(VideoID.from(id));
    }
}