package com.fullcycle.admin.catalogo.application.castmember.update;

import static org.mockito.Mockito.times;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.fullcycle.admin.catalogo.application.Fixture;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;

public class UpdateCastMemberUseCaseTest extends UseCaseTest{

    @InjectMocks
    private DefaultUpdateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGatway;
    
    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGatway);
    }
    //__________________________________________________________________________

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentuifier(){
        //given
        final var aMember = CastMember.newMember("nome", CastMemberType.ACTOR);
        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.DIRECTOR;

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);
        
        Mockito.when(castMemberGatway.findById(Mockito.any())).thenReturn(Optional.of(CastMember.with(aMember)));
        Mockito.when(castMemberGatway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        final var actualOutput = useCase.execute(command);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(castMemberGatway).findById(Mockito.eq(expectedId));

        Mockito.verify(castMemberGatway).update(Mockito.argThat(memberUpdate -> 
            Objects.equals(expectedId, memberUpdate.getId())
            && Objects.equals(expectedName, memberUpdate.getName())
            && Objects.equals(expectedType, memberUpdate.getType())
            && Objects.equals(aMember.getCreatedAt(), memberUpdate.getCreatedAt())
            && aMember.getUpdatedAt().isBefore(memberUpdate.getUpdatedAt())
        ));
    }
    
    @Test
    public void givenAnInvalidName_whenCallsUpdateCastMember_shouldReturnThrowsNotification(){
        //given
        final var aMember = CastMember.newMember("nome", CastMemberType.ACTOR);
        final var expectedId = aMember.getId();
        final String expectedName = null;
        final var expectedType = CastMemberType.DIRECTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);
        
        Mockito.when(castMemberGatway.findById(Mockito.any())).thenReturn(Optional.of(aMember));

        //when
        final var actualException = Assertions.assertThrows(NotificationException.class, ()-> useCase.execute(command));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGatway).findById(Mockito.eq(expectedId));
        Mockito.verify(castMemberGatway, times(0)).update(Mockito.any());
    }
    
    @Test
    public void givenAnInvalidType_whenCallsUpdateCastMember_shouldReturnThrowsNotification(){
        //given
        final var aMember = CastMember.newMember("nome", CastMemberType.ACTOR);
        final var expectedId = aMember.getId();
        final String expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);
        
        Mockito.when(castMemberGatway.findById(Mockito.any())).thenReturn(Optional.of(aMember));

        //when
        final var actualException = Assertions.assertThrows(NotificationException.class, ()-> useCase.execute(command));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGatway).findById(Mockito.eq(expectedId));
        Mockito.verify(castMemberGatway, times(0)).update(Mockito.any());
    }
    
    @Test
    public void givenAnInvalidId_whenCallsUpdateCastMember_shouldReturnThrowsNotification(){
        //given
        final var expectedId = CastMemberID.from("123");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);
        
        Mockito.when(castMemberGatway.findById(Mockito.any())).thenReturn(Optional.empty());

        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class, ()-> useCase.execute(command));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(castMemberGatway).findById(Mockito.eq(expectedId));
        Mockito.verify(castMemberGatway, times(0)).update(Mockito.any());
    }
}