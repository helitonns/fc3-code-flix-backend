package com.fullcycle.admin.catalogo.domain.video;

import java.time.Year;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.entity.video.Rating;
import com.fullcycle.admin.catalogo.domain.entity.video.Video;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;

public class VideoValidatorTest {

    @Test
    public void givenNullTitle_whenCallsValidator_shouldReceiveError(){
        //given
        final String expectedTitle = null;
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

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be null";

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
        
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());
        
        //when
        final var actualError = Assertions.assertThrows(DomainException.class, ()-> validator.validate());

        //then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyTitle_whenCallsValidator_shouldReceiveError(){
        //given
        final var expectedTitle = "";
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

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

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
        
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());
        
        //when
        final var actualError = Assertions.assertThrows(DomainException.class, ()-> validator.validate());

        //then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenTitleWithLengthGreater255_whenCallsValidator_shouldReceiveError(){
        //given
        final var expectedTitle = """
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            """;;
        final var expectedDescription = """
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br.
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' must be between 1 and 255 characters";

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
        
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());
        
        //when
        final var actualError = Assertions.assertThrows(DomainException.class, ()-> validator.validate());

        //then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullDescription_whenCallsValidator_shouldReceiveError(){
        //given
        final var expectedTitle = "System Design Interview";
        final String expectedDescription = null;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' sholud not be null";

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
        
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());
        
        //when
        final var actualError = Assertions.assertThrows(DomainException.class, ()-> validator.validate());

        //then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyDescription_whenCallsValidator_shouldReceiveError(){
        //given
        final var expectedTitle = "System Design Interview";
        final String expectedDescription = "";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' sholud not be empty";

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
        
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());
        
        //when
        final var actualError = Assertions.assertThrows(DomainException.class, ()-> validator.validate());

        //then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenDescriptionWithLengthGreater4000_whenCallsValidator_shouldReceiveError(){
        //given
        final var expectedTitle = "System Design Interview";
        final String expectedDescription = """
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            """;
        final Year expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' must be between 1 and 4000 characters";;

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
        
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());
        
        //when
        final var actualError = Assertions.assertThrows(DomainException.class, ()-> validator.validate());

        //then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullLaunchedAt_whenCallsValidator_shouldReceiveError(){
        //given
        final var expectedTitle = "System Design Interview";
        final String expectedDescription = """
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            """;
        final Year expectedLaunchedAt = null;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'launchedAt' sholud not be null";

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
        
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());
        
        //when
        final var actualError = Assertions.assertThrows(DomainException.class, ()-> validator.validate());

        //then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullRating_whenCallsValidator_shouldReceiveError(){
        //given
        final var expectedTitle = "System Design Interview";
        final String expectedDescription = """
            O estudo apresentadeo apresentado tem fins educaionais e representa nossas opiniões pessoais.
            Esse vídeo faz parte da Imessão Full Stack && Full Cycle. Para acessar todas as aulas, lives
            e dessafios, acesse: https://imerssao.fullcycle.com.br
            """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final Rating expectedRating = null;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rating' sholud not be null";

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
        
        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());
        
        //when
        final var actualError = Assertions.assertThrows(DomainException.class, ()-> validator.validate());

        //then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

}