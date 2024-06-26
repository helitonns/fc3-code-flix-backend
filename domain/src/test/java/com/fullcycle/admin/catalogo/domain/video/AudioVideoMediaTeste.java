package com.fullcycle.admin.catalogo.domain.video;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fullcycle.admin.catalogo.domain.entity.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.entity.video.MediaStatus;

public class AudioVideoMediaTeste {
@Test
  public void givenValidParams_whenCallsNewAudioVideo_shouldReturnInstance(){
    //given
    final var expectedChecksum = "123";
    final var expectedName = "Banner.png";
    final var expectedRawLocation = "/imeges/123";
    final var expectedEncodedLocation = "/imeges/123";
    final var expectedStatus = MediaStatus.PENDING;

    //when
    final var actualVideo = AudioVideoMedia.with(
      expectedChecksum, 
      expectedName, 
      expectedRawLocation,
      expectedEncodedLocation,
      expectedStatus
    );

    //then
    Assertions.assertNotNull(actualVideo);
    Assertions.assertEquals(expectedChecksum, actualVideo.checksum());
    Assertions.assertEquals(expectedName, actualVideo.name());
    Assertions.assertEquals(expectedRawLocation, actualVideo.rawLocation());
    Assertions.assertEquals(expectedEncodedLocation, actualVideo.encodedLocation());
    Assertions.assertEquals(expectedStatus, actualVideo.status());
  }
  
  @Test
  public void givenTwoVideosWithSameChecksumAndLocation_whenCallsEquals_shouldReturnTrue(){
    //given
    final var expectedChecksum = "123";
    final var expectedRawLocation = "/imeges/123";

    
    final var video1 = AudioVideoMedia.with(
      expectedChecksum, 
      "132", 
      expectedRawLocation,
      "13",
      MediaStatus.COMPLETED
    );

    final var video2 = AudioVideoMedia.with(
      expectedChecksum, 
      "132", 
      expectedRawLocation,
      "13",
      MediaStatus.COMPLETED
    );

    //then
    Assertions.assertEquals(video1, video2);
    Assertions.assertFalse(video1 == video2);
  }
  
  @Test
  public void givenInvalidParams_whenCallsWith_shouldReturnError(){
    Assertions.assertThrows(
      NullPointerException.class, 
      ()-> AudioVideoMedia.with(
        null, 
        "123", 
        "123",
        "123",
        MediaStatus.COMPLETED
      )
    );
    Assertions.assertThrows(
      NullPointerException.class, 
      ()-> AudioVideoMedia.with(
        "123", 
        null, 
        "123",
        "123",
        MediaStatus.COMPLETED
      )
    );
    Assertions.assertThrows(
      NullPointerException.class, 
      ()-> AudioVideoMedia.with(
        "123", 
        "123", 
        null,
        "123",
        MediaStatus.COMPLETED
      )
    );
    Assertions.assertThrows(
      NullPointerException.class, 
      ()-> AudioVideoMedia.with(
        "123", 
        "123", 
        "123",
        null,
        MediaStatus.COMPLETED
      )
    );
    Assertions.assertThrows(
      NullPointerException.class, 
      ()-> AudioVideoMedia.with(
        "123", 
        "123", 
        "123",
        "123",
        null
      )
    );
  }
}
