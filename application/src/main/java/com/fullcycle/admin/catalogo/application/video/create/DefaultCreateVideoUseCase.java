package com.fullcycle.admin.catalogo.application.video.create;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.entity.video.MediaResourceGateway;
import com.fullcycle.admin.catalogo.domain.entity.video.Rating;
import com.fullcycle.admin.catalogo.domain.entity.video.Video;
import com.fullcycle.admin.catalogo.domain.entity.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;

public class DefaultCreateVideoUseCase extends CreateVideoUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;
    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    //__________________________________________________________________________
    public DefaultCreateVideoUseCase(
        final CategoryGateway categoryGateway, 
        final GenreGateway genreGateway,
        final CastMemberGateway castMemberGateway, 
        final VideoGateway videoGateway,
        final MediaResourceGateway mediaResourceGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public CreateVideoOutput execute(final CreateVideoCommand command) {
        final var rating = Rating.of(command.rating()).orElseThrow(invalidRating(command.rating()));
        final var launchYear = Year.of(command.launchedAt());
        final var categories = toIdentifier(command.categories(), CategoryID::from);
        final var genres = toIdentifier(command.genres(), GenreID::from);
        final var members = toIdentifier(command.members(), CastMemberID::from);

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateMembers(members));

        final var video = Video.newVideo(
            command.title(),
            command.description(), 
            launchYear, 
            command.duration(), 
            rating, 
            command.opened(), 
            command.plublished(), 
            categories, 
            genres,
            members 
        );

        video.validate(notification);

        if(notification.hasError()){
            throw new NotificationException("Could not create Aggregate", notification);
        }
        
        return CreateVideoOutput.from(create(command, video));   
    }

    private Video create(final CreateVideoCommand command, final Video video){
        final var idVideo = video.getId();

        try {
            final var videoMedia = command.getVideo().map(it -> this.mediaResourceGateway.storeAudioVideo(idVideo, it)).orElse(null);
            final var trailerMedia = command.getTrailer().map(it -> this.mediaResourceGateway.storeAudioVideo(idVideo, it)).orElse(null);
            final var bannerMedia = command.getBanner().map(it -> this.mediaResourceGateway.storeImage(idVideo, it)).orElse(null);
            final var thumbnailMedia = command.getThumbnail().map(it -> this.mediaResourceGateway.storeImage(idVideo, it)).orElse(null);
            final var thumbnailHalfMedia = command.getThumbnailHalf().map(it -> this.mediaResourceGateway.storeImage(idVideo, it)).orElse(null);

            return this.videoGateway.create(
                video
                .setVideo(videoMedia)
                .setTrailer(trailerMedia)
                .setBanner(bannerMedia)
                .setThumbnail(thumbnailMedia)
                .setThumbnailHalf(thumbnailHalfMedia)
            );
        } catch (final Throwable t) {
            this.mediaResourceGateway.clearResource(idVideo);
            throw InternalErrorException.with("An error on create video was observed [videoId: %s]".formatted(idVideo.getValue()), t);
        }
    }
    
    private Supplier<DomainException> invalidRating(final String rating){
        return ()-> DomainException.with(new Error("Rating not found %s".formatted(rating)));
    }

    private <T> Set<T> toIdentifier(final Set<String> ids, final Function<String, T> mapper){
        return ids.stream()
            .map(mapper)
            .collect(Collectors.toSet());
    }

    private ValidationHandler validateCategories(final Set<CategoryID> ids){
        return validateAggregate("categories", ids, categoryGateway::existsByIds);
    }

    private ValidationHandler validateGenres(final Set<GenreID> ids){
        return validateAggregate("genres", ids, genreGateway::existsByIds);
    }

    private ValidationHandler validateMembers(final Set<CastMemberID> ids){
        return validateAggregate("cast members", ids, castMemberGateway::existsByIds);
    }

    private <T extends Identifier> ValidationHandler validateAggregate(
        final String aggregate,
        final Set<T> ids,
        final Function<Iterable<T>, List<T>> existById
    ){
        final var notification = Notification.create();

        if (ids == null || ids.isEmpty()){
            return notification;
        }

        final var retrievedIds = existById.apply(ids);

        if (ids.size() != retrievedIds.size()) {
            final var commandIds = new ArrayList<>(ids);
            commandIds.removeAll(retrievedIds);

            final var missingIds = commandIds.stream()
                .map(Identifier::getValue)
                .collect(Collectors.joining(", "));

            notification.append(new Error("Some %s could not be found: %s".formatted(aggregate, missingIds)));
        }

        return notification;
    }

}