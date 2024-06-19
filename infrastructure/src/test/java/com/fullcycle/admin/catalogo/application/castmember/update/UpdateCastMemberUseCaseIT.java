package com.fullcycle.admin.catalogo.application.castmember.update;

import static org.mockito.Mockito.times;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import com.fullcycle.admin.catalogo.Fixture;
import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.persistence.CastMemberRepository;

@IntegrationTest
public class UpdateCastMemberUseCaseIT {

    @Autowired
    private UpdateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository repository;

    @SpyBean
    private CastMemberGateway gateway;
    
    //__________________________________________________________________________

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentuifier(){
        //given
        final var member = CastMember.newMember("nome", CastMemberType.ACTOR);
        repository.saveAndFlush(CastMemberJpaEntity.from(member));

        final var expectedId = member.getId();
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.DIRECTOR;

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);
        
        //when
        final var actualOutput = useCase.execute(command);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualPersistedMember = repository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualPersistedMember.getName());
        Assertions.assertEquals(expectedType, actualPersistedMember.getType());
        //Assertions.assertEquals(member.getCreatedAt(), actualPersistedMember.getCreatedAt());
        Assertions.assertTrue(member.getUpdatedAt().isBefore(actualPersistedMember.getUpdatedAt()));

        Mockito.verify(gateway).findById(Mockito.any());
        Mockito.verify(gateway).update(Mockito.any());
    }
    
    @Test
    public void givenAnInvalidName_whenCallsUpdateCastMember_shouldReturnThrowsNotification(){
        //given
        final var member = CastMember.newMember("nome", CastMemberType.ACTOR);
        repository.saveAndFlush(CastMemberJpaEntity.from(member));

        final var expectedId = member.getId();
        final String expectedName = null;
        final var expectedType = CastMemberType.DIRECTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);
        
        //when
        final var actualException = Assertions.assertThrows(NotificationException.class, ()-> useCase.execute(command));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(gateway).findById(Mockito.any());
        Mockito.verify(gateway, times(0)).update(Mockito.any());
    }
    
    @Test
    public void givenAnInvalidType_whenCallsUpdateCastMember_shouldReturnThrowsNotification(){
        //given
        final var member = CastMember.newMember("nome", CastMemberType.ACTOR);
        repository.saveAndFlush(CastMemberJpaEntity.from(member));

        final var expectedId = member.getId();
        final String expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);
        
        //when
        final var actualException = Assertions.assertThrows(NotificationException.class, ()-> useCase.execute(command));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(gateway).findById(Mockito.any());
        Mockito.verify(gateway, times(0)).update(Mockito.any());
    }
    
    @Test
    public void givenAnInvalidId_whenCallsUpdateCastMember_shouldReturnThrowsNotification(){
        //given
        final var expectedId = CastMemberID.from("123");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var command = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);
        
        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class, ()-> useCase.execute(command));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(gateway).findById(Mockito.any());
        Mockito.verify(gateway, times(0)).update(Mockito.any());
    }
}