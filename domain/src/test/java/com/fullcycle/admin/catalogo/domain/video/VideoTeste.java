package com.fullcycle.admin.catalogo.domain.video;

import java.time.Year;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.entity.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.entity.video.ImageMedia;
import com.fullcycle.admin.catalogo.domain.entity.video.MediaStatus;
import com.fullcycle.admin.catalogo.domain.entity.video.Rating;
import com.fullcycle.admin.catalogo.domain.entity.video.Video;
import com.fullcycle.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;

public class VideoTeste {

    @Test
    public void givenValidParams_whenCallsNewVideo_shouldInstantiate(){
        //given
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        //when
        final var actualVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPublished,
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        //then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertNotNull(actualVideo.getCreatedAt());
        Assertions.assertNotNull(actualVideo.getUpdatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(()-> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdate_shouldReturnUpdate(){
        //given
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var video = Video.newVideo(
            "teste title",
            "description",
            Year.of(2000),
            1.10,
            Rating.AGE_10,
            true,
            true,
            Set.of(),
            Set.of(),
            Set.of()
        );
        
        //when
        final var actualVideo = Video.with(video).update(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPublished,
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        //then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(video.getCreatedAt(), actualVideo.getCreatedAt());
        Assertions.assertTrue(video.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(()-> actualVideo.validate(new ThrowsValidationHandler()));
    }
    
    @Test
    public void givenValidVideo_whenCallsSetVideo_shouldReturnUpdate(){
        //given
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var video = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPublished,
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        final var videoMedia = AudioVideoMedia.with("abc", "video.mp4", "/123/video", "", MediaStatus.PENDING);
        
        //when
        final var actualVideo = Video.with(video).setVideo(videoMedia);

        //then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(video.getCreatedAt(), actualVideo.getCreatedAt());
        Assertions.assertTrue(video.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertEquals(videoMedia, actualVideo.getVideo());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(()-> actualVideo.validate(new ThrowsValidationHandler()));
    }
    
    @Test
    public void givenValidVideo_whenCallsSetTailer_shouldReturnUpdate(){
        //given
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var video = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPublished,
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        final var trailerMedia = AudioVideoMedia.with("abc", "video.mp4", "/123/video", "", MediaStatus.PENDING);
        
        //when
        final var actualVideo = Video.with(video).setTrailer(trailerMedia);

        //then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(video.getCreatedAt(), actualVideo.getCreatedAt());
        Assertions.assertTrue(video.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertEquals(trailerMedia, actualVideo.getTrailer());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(()-> actualVideo.validate(new ThrowsValidationHandler()));
    }
    
    @Test
    public void givenValidVideo_whenCallsSetBanner_shouldReturnUpdate(){
        //given
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var video = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPublished,
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        final var banner = ImageMedia.with("abc", "banner", "/image");
        
        //when
        final var actualVideo = Video.with(video).setBanner(banner);

        //then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(video.getCreatedAt(), actualVideo.getCreatedAt());
        Assertions.assertTrue(video.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertEquals(banner, actualVideo.getBanner();
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(()-> actualVideo.validate(new ThrowsValidationHandler()));
    }
    
    @Test
    public void givenValidVideo_whenCallsSetThumbnail_shouldReturnUpdate(){
        //given
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var video = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPublished,
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        final var thumbMedia = ImageMedia.with("abc", "banner", "/image");
        
        //when
        final var actualVideo = Video.with(video).setThumbnail(thumbMedia);

        //then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(video.getCreatedAt(), actualVideo.getCreatedAt());
        Assertions.assertTrue(video.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertEquals(thumbMedia, actualVideo.getThumbnail());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(()-> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsSetThumbnailHalf_shouldReturnUpdate(){
        //given
        final var expectedTitle = "System Design Interview";
        final var expectedDescription = """
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var video = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPublished,
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        final var thumbMedia = ImageMedia.with("abc", "banner", "/image");
        
        //when
        final var actualVideo = Video.with(video).setThumbnailHalf(thumbMedia);

        //then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(video.getCreatedAt(), actualVideo.getCreatedAt());
        Assertions.assertTrue(video.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertEquals(thumbMedia, actualVideo.getThumbnailHalf());

        Assertions.assertDoesNotThrow(()-> actualVideo.validate(new ThrowsValidationHandler()));
    }
}