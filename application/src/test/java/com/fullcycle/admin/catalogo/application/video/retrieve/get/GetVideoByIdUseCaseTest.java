package com.fullcycle.admin.catalogo.application.video.retrieve.get;

import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.fullcycle.admin.catalogo.application.Fixture;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.entity.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.entity.video.ImageMedia;
import com.fullcycle.admin.catalogo.domain.entity.video.MediaStatus;
import com.fullcycle.admin.catalogo.domain.entity.video.Resource;
import com.fullcycle.admin.catalogo.domain.entity.video.Video;
import com.fullcycle.admin.catalogo.domain.entity.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.entity.video.VideoID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;

public class GetVideoByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetVideoByIdUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }
    //__________________________________________________________________________

    @Test
    public void givenAValidId_whenCallsGetVideo_shouldReturnIt(){
        //given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPlublished = Fixture.bool();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.wesley().getId(), Fixture.CastMembers.heliton().getId());

        final var expectedVideo = audioVideo(Resource.Type.VIDEO);
        final var expectedTrailer = audioVideo(Resource.Type.TRAILER);
        final var expectedBanner = image(Resource.Type.BANNER);
        final var expectedThumb = image(Resource.Type.THUMBNAIL);
        final var expectedThumbHalf = image(Resource.Type.THUMBNAIL_HALF);

        final var video = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPlublished,
            expectedCategories,
            expectedGenres,
            expectedMembers)
            .setVideo(expectedVideo)
            .setTrailer(expectedTrailer)
            .setBanner(expectedBanner)
            .setThumbnail(expectedThumb)
            .setThumbnailHalf(expectedThumbHalf);

        final var id = video.getId();

        Mockito.when(videoGateway.findbyId(Mockito.any())).thenReturn(Optional.of(Video.with(video)));

        //when
        final var actualVideo = this.useCase.execute(id.getValue());

        //then
        Assertions.assertEquals(id.getValue(), actualVideo.getId());
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
        Assertions.assertEquals(expectedPlublished, actualVideo.getPublished());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumb, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbHalf, actualVideo.thumbnailHalf());
        Assertions.assertEquals(video.getCreatedAt(), actualVideo.createdAt());
        Assertions.assertEquals(video.getUpdatedAt(), actualVideo.updatedAt());
    }

    @Test
    public void givenInvalidid_whenCallsGetVideo_shouldReturnNotFound(){
        //given
        final var expectedErrorMessage = "Video with ID 123 not found";

        final var expectedId = VideoID.from("123");

        Mockito.when(videoGateway.findbyId(Mockito.any())).thenReturn(Optional.empty());

        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class, ()-> this.useCase.execute(expectedId.getValue()));

        //then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private AudioVideoMedia audioVideo(final Resource.Type type){
        final var checksum = UUID.randomUUID().toString();
        
        return AudioVideoMedia.with(
            checksum, 
            type.name().toLowerCase(), 
            "/videos/" + checksum, 
            "", 
            MediaStatus.PENDING
        );
    }

    private ImageMedia image(final Resource.Type type){
        final var checksum = UUID.randomUUID().toString();
        
        return ImageMedia.with(
            checksum, 
            type.name().toLowerCase(), 
            "/images/" + checksum
        );
    }
}