package com.fullcycle.admin.catalogo.application.castmember.create;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import com.fullcycle.admin.catalogo.Fixture;
import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.persistence.CastMemberRepository;

@IntegrationTest
public class CreateCastMemberUseCaseIT{

    @Autowired
    private CreateCastMemberUseCase useCase;
    
    @Autowired
    private CastMemberRepository repository;

    @SpyBean
    private CastMemberGateway gatway;
    
    //__________________________________________________________________________

    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_shouldReturnIt(){
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        //when
        final var actualOutput = useCase.execute(command);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualMember = this.repository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
        Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());

        Mockito.verify(gatway).create(Mockito.any());
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCastMember_shouldThrowsNotificationException(){
        //given
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        //when
        final var actualException = Assertions
            .assertThrows(NotificationException.class, ()-> useCase.execute(command));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
    
    @Test
    public void givenAnInvalidType_whenCallsCreateCastMember_shouldThrowsNotificationException(){
        //given
        final String expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        //when
        final var actualException = Assertions
            .assertThrows(NotificationException.class, ()-> useCase.execute(command));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }



}