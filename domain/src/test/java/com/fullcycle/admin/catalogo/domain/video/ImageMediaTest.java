package com.fullcycle.admin.catalogo.domain.video;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fullcycle.admin.catalogo.domain.entity.video.ImageMedia;

public class ImageMediaTest {

  @Test
  public void givenValidParams_whenCalls_shouldReturnInstance(){
    //given
    final var expectedChecksum = "123";
    final var expectedName = "Banner.png";
    final var expectedLocation = "/imeges/123";

    //when
    final var actualImage = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

    //then
    Assertions.assertNotNull(actualImage);
    Assertions.assertEquals(expectedChecksum, actualImage.checksum());
    Assertions.assertEquals(expectedName, actualImage.name());
    Assertions.assertEquals(expectedLocation, actualImage.location());
  }
  
  @Test
  public void givenTwoImagesWithSameChecksumAndLocation_whenCallsEquals_shouldReturnTrue(){
    //given
    final var expectedChecksum = "123";
    final var expectedLocation = "/imeges/123";

    final var img1 = ImageMedia.with(expectedChecksum, "123", expectedLocation);
    final var img2 = ImageMedia.with(expectedChecksum, "465", expectedLocation);

    //then
    Assertions.assertEquals(img1, img2);
    Assertions.assertFalse(img1 == img2);
  }
  
  @Test
  public void givenInvalidParams_whenCallsWith_shouldReturnError(){
    Assertions.assertThrows(
      NullPointerException.class, 
      ()-> ImageMedia.with(null, "name", "/image")
    );
    Assertions.assertThrows(
      NullPointerException.class, 
      ()-> ImageMedia.with("132", null, "/image")
    );
    Assertions.assertThrows(
      NullPointerException.class, 
      ()-> ImageMedia.with("132", "ad", null)
    );
  }


}
