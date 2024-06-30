package com.fullcycle.admin.catalogo.application.video.create;

import java.util.Optional;
import java.util.Set;
import com.fullcycle.admin.catalogo.domain.entity.video.Resource;

public record CreateVideoCommand(
    String title,
    String description,
    int launchedAt,
    double duration,
    boolean opened,
    boolean plublished,
    String rating,
    Set<String> categories,
    Set<String> genres,
    Set<String> members,
    Resource video,
    Resource trailer,
    Resource banner,
    Resource thumbnail,
    Resource thumbnialHalf
) {
    public static CreateVideoCommand with(
        final String title,
        final String description,
        final int launchedAt,
        final double duration,
        final boolean opened,
        final boolean plublished,
        final String rating,
        final Set<String> categories,
        final Set<String> genres,
        final Set<String> members,
        final Resource video,
        final Resource trailer,
        final Resource banner,
        final Resource thumbnail,
        final Resource thumbnialHalf
    ){
        return new CreateVideoCommand(title, description, launchedAt, duration, opened, plublished, rating, categories, genres, members, video, trailer, banner, thumbnail, thumbnialHalf);
    }

    public Optional<Resource> getVideo(){
        return Optional.ofNullable(video);
    }
    
    public Optional<Resource> getTrailer(){
        return Optional.ofNullable(trailer);
    }
    
    public Optional<Resource> getBanner(){
        return Optional.ofNullable(banner);
    }
    
    public Optional<Resource> getThumbnail(){
        return Optional.ofNullable(thumbnail);
    }
    
    public Optional<Resource> getThumbnailHalf(){
        return Optional.ofNullable(thumbnialHalf);
    }
}