package com.fullcycle.admin.catalogo.application.video.create;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.fullcycle.admin.catalogo.application.Fixture;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.entity.category.Category;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.entity.video.Video;
import com.fullcycle.admin.catalogo.domain.entity.video.VideoGateway;
import org.mockito.AdditionalAnswers;

public class CreateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;
    
    @Mock
    private CategoryGateway categoryGateway;
    
    @Mock
    private GenreGateway genreGateway;
    
    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }
    
    //__________________________________________________________________________

    @Test
    public void givenAValidCommand_whenCallsCreateVideo_shouldReturnVideoId(){
        //given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchedAt = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedOpened = Fixture.bool();
        final var expectedPlublished = Fixture.bool();
        final var expectedCategories = Set.of(Fixture.Categories.aulas());
        final var expectedGenres = Set.of(Fixture.Genres.tech());
        final var expectedMembers = Set.of(Fixture.CastMembers.wesley().getId(), Fixture.CastMembers.heliton().getId();
        
        final Resource expectedVideo = Fixture.Videos.resource(Type.VEDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Type.TUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Type.TUMBNAIL_HALF);

        final var command = CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPlublished,
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf
        );

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedCategories));
        Mockito.when(genreGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedBanner));
        Mockito.when(castMemberGateway.existsByIds(Mockito.any())).thenReturn(new ArrayList<>(expectedMembers));
        Mockito.when(videoGateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());
        //when
        final var actualResult = useCase.excute(command);

        //then
        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.id();

        Mockito.verify(videoGateway).create(Mockito.argThat(actualVideo -> 
            Objects.equals(expectedTitle, actualVideo.getTitle())
            && Objects.equals(expectedDescription, actualVideo.getDescription())
            && Objects.equals(expectedLaunchedAt, actualVideo.getLaunchedAt())
            && Objects.equals(expectedDuration, actualVideo.getDuration())
            && Objects.equals(expectedRating, actualVideo.getRating())
            && Objects.equals(expectedOpened, actualVideo.getOpened())
            && Objects.equals(expectedPlublished, actualVideo.getPublished())
            && Objects.equals(expectedCategories, actualVideo.getCategories)
            && Objects.equals(expectedGenres, actualVideo.getGenres())
            && Objects.equals(expectedMembers, actualVideo.getCastMembers())
            && actualVideo.getVideo().isPresent()
            && actualVideo.getTrailer().isPresent()
            && actualVideo.getBanner().isPresent()
            && actualVideo.getThumbnail().isPresent()
            && actualVideo.getThumbnailHalf().isPresent()
        ));

    }




}