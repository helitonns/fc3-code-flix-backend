package com.fullcycle.admin.catalogo.domain.entity.video;

import java.time.Instant;
import java.time.Year;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;

public class Video extends AggregateRoot<VideoID> {

    private String title;
    private String description;
    private Year launchedAt;
    private double duration;
    private Rating rating;

    private boolean opened;
    private boolean published;
    
    private Instant createdAt;
    private Instant updatedAt;
    
    private ImageMedia banner;
    private ImageMedia thumbnail;
    private ImageMedia thumbnailHalf;
    
    private AudioVideoMedia trailer;
    private AudioVideoMedia video;
    
    private Set<CategoryID> categories;
    private Set<GenreID> genres;
    private Set<CastMemberID> castMembers;

    protected Video(
        final VideoID id,
        final String title,
        final String description,
        final Year launchedAt,
        final double duration,
        final Rating rating,
        final boolean opened,
        final boolean published,
        final Instant createdAt,
        final Instant updatedAt,
        final ImageMedia banner,
        final ImageMedia thumbnail,
        final ImageMedia thumbnailHalf,
        final AudioVideoMedia trailer,
        final AudioVideoMedia video,
        final Set<CategoryID> categories,
        final Set<GenreID> genres,
        final Set<CastMemberID> castMembers
    ) {
        super(id);
        this.title = title;
        this.description = description;
        this.launchedAt = launchedAt;
        this.duration = duration;
        this.rating = rating;
        this.opened = opened;
        this.published = published;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.banner = banner;
        this.thumbnail = thumbnail;
        this.thumbnailHalf = thumbnailHalf;
        this.trailer = trailer;
        this.video = video;
        this.categories = categories;
        this.genres = genres;
        this.castMembers = castMembers;
    }

    public static Video newVideo(
        final String title,
        final String description,
        final Year launchedAt,
        final double duration,
        final Rating rating,
        final boolean opened,
        final boolean published,
        final Set<CategoryID> categories,
        final Set<GenreID> genres,
        final Set<CastMemberID> castMembers
    ){
        final var id = VideoID.unique();
        final var now = InstantUtils.now();

        return new Video(
            id, 
            title, 
            description, 
            launchedAt, 
            duration, 
            rating, 
            opened, 
            published, 
            now, 
            now, 
            null, 
            null, 
            null, 
            null, 
            null, 
            categories, 
            genres, 
            castMembers
        );
    }
    
    public static Video with(final Video video){
        return new Video(
            video.getId(), 
            video.getTitle(), 
            video.getDescription(), 
            video.getLaunchedAt(), 
            video.getDuration(), 
            video.getRating(), 
            video.getOpened(), 
            video.getPublished(), 
            video.getCreatedAt(), 
            video.getUpdatedAt(), 
            video.getBanner().orElse(null), 
            video.getThumbnail().orElse(null), 
            video.getThumbnailHalf().orElse(null), 
            video.getTrailer().orElse(null), 
            video.getVideo().orElse(null), 
            new HashSet<>(video.getCategories()), 
            new HashSet<>(video.getGenres()), 
            new HashSet<>(video.getCastMembers())
        );
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Year getLaunchedAt() {
        return launchedAt;
    }

    public double getDuration() {
        return duration;
    }

    public Rating getRating() {
        return rating;
    }

    public boolean getOpened() {
        return opened;
    }

    public boolean getPublished() {
        return published;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Optional<ImageMedia> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<ImageMedia> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<ImageMedia> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }

    public Optional<AudioVideoMedia> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<AudioVideoMedia> getVideo() {
        return Optional.ofNullable(video);
    }

    public Set<CategoryID> getCategories() {
        return categories != null ? Collections.unmodifiableSet(categories) : Collections.emptySet();
    }

    public Set<GenreID> getGenres() {
        return genres != null ? Collections.unmodifiableSet(genres) : Collections.emptySet();
    }

    public Set<CastMemberID> getCastMembers() {
        return castMembers != null ? Collections.unmodifiableSet(castMembers) : Collections.emptySet();
    }

    public Video setBanner(final ImageMedia banner) {
        this.banner = banner;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video setThumbnail(final ImageMedia thumbnail) {
        this.thumbnail = thumbnail;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video setThumbnailHalf(final ImageMedia thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video setTrailer(final AudioVideoMedia trailer) {
        this.trailer = trailer;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video setVideo(final AudioVideoMedia video) {
        this.video = video;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    private void setCategories(final Set<CategoryID> categories) {
        this.categories = categories != null ? new HashSet<>(categories) : Collections.emptySet();
    }

    private void setGenres(final Set<GenreID> genres) {
        this.genres = genres != null ? new HashSet<>(genres) : Collections.emptySet();
    }

    private void setCastMembers(final Set<CastMemberID> castMembers) {
        this.castMembers = castMembers != null ? new HashSet<>(castMembers) : Collections.emptySet();
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new VideoValidator(this, handler).validate();
    }

    public void update(
        final String title,
        final String description,
        final Year launchedAt,
        final double duration,
        final Rating rating,
        final boolean opened,
        final boolean published,
        final Set<CategoryID> categories,
        final Set<GenreID> genres,
        final Set<CastMemberID> castMembers
    ) {
        this.title = title;
        this.description = description;
        this.launchedAt = launchedAt;
        this.duration = duration;
        this.rating = rating;
        this.opened = opened;
        this.published = published;
        this.setCategories(categories);
        this.setGenres(genres);
        this.setCastMembers(castMembers);

        this.updatedAt = InstantUtils.now();
    }
}