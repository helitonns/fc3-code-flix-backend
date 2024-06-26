package com.fullcycle.admin.catalogo.application.castmember.create;

import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.fullcycle.admin.catalogo.application.Fixture;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;

public class CreateCastMemberUseCaseTest extends UseCaseTest{

    @InjectMocks
    private DefaultCreateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGatway;
    
    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGatway);
    }
    //__________________________________________________________________________

    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_shouldReturnIt(){
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var command = CreateCastMemberCommand.with(expectedName, expectedType);

        Mockito.when(castMemberGatway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());
        
        //when
        final var actualOutput = useCase.execute(command);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(castMemberGatway).create(Mockito.argThat(member -> 
            Objects.nonNull(member.getId())
            && Objects.equals(expectedName, member.getName())
            && Objects.equals(expectedType, member.getType())
            && Objects.nonNull(member.getCreatedAt())
            && Objects.nonNull(member.getUpdatedAt())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCastMember_shouldThrowsNotificationException(){
        //given
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
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