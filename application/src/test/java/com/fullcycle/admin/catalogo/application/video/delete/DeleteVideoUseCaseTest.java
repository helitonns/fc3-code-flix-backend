package com.fullcycle.admin.catalogo.application.video.delete;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.entity.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.entity.video.VideoID;
import com.fullcycle.admin.catalogo.domain.exceptions.InternalErrorException;

public class DeleteVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }
    //__________________________________________________________________________

    @Test
    public void givenAValid_whenCallsDeleteVideo_shouldDeleteIt(){
        //given
        final var expectedId = VideoID.unique();

        Mockito.doNothing().when(videoGateway).deletebyId(Mockito.any());
        
        //when
        Assertions.assertDoesNotThrow(()-> this.useCase.execute(expectedId.getValue()));

        //then
        Mockito.verify(videoGateway).deletebyId(Mockito.eq(expectedId));
    }

    @Test
    public void givenAnInvalid_whenCallsDeleteVideo_shouldBeOk(){
        //given
        final var expectedId = VideoID.from("123");

        Mockito.doNothing().when(videoGateway).deletebyId(Mockito.any());
        
        //when
        Assertions.assertDoesNotThrow(()-> this.useCase.execute(expectedId.getValue()));

        //then
        Mockito.verify(videoGateway).deletebyId(Mockito.eq(expectedId));
    }

    @Test
    public void givenAValid_whenCallsDeleteVideoAndGatewayThrowsException_shouldReceiveException(){
        //given
        final var expectedId = VideoID.from("123");

        Mockito.doThrow(InternalErrorException.with("Error on delete video", new RuntimeException()))
        .when(videoGateway).deletebyId(Mockito.any());
        
        //when
        Assertions.assertThrows(InternalErrorException.class, ()-> this.useCase.execute(expectedId.getValue()));

        //then
        Mockito.verify(videoGateway).deletebyId(Mockito.eq(expectedId));
    }

}