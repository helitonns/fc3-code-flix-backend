package com.fullcycle.admin.catalogo.application.video.create;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.fullcycle.admin.catalogo.application.Fixture;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.entity.video.Video;
import com.fullcycle.admin.catalogo.domain.entity.video.VideoGateway;

public class CreateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

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
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        //when


        //then



    }




}