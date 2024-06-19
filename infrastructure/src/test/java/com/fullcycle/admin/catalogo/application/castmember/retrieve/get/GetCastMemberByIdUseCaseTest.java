package com.fullcycle.admin.catalogo.application.castmember.retrieve.get;

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
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.persistence.CastMemberRepository;

@IntegrationTest
public class GetCastMemberByIdUseCaseTest  {
    
    @Autowired
    private GetCastMemberByIdUseCase useCase;
    
    @Autowired
    private CastMemberRepository repository;

    @SpyBean
    private CastMemberGateway gateway;
    
    //__________________________________________________________________________

    @Test
    public void givenAValidId_whenCallsGetCastMember_shouldReturnIt(){
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();

        this.repository.saveAndFlush(CastMemberJpaEntity.from(member));
        
        Assertions.assertEquals(1, repository.count());

        //when
        final var actualOutput = useCase.execute(expectedId.getValue());

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedType, actualOutput.type());
        Assertions.assertEquals(member.getCreatedAt(), actualOutput.createdAt());
        Assertions.assertEquals(member.getUpdatedAt(), actualOutput.updatedAt());

        Mockito.verify(gateway).findById(Mockito.any());
    }

    @Test
    public void givenAnInvalidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFoundException(){
        //given
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        //when
        final var actualOutput = Assertions.assertThrows(NotFoundException.class, ()-> useCase.execute(expectedId.getValue()));

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

        Mockito.verify(gateway).findById(Mockito.eq(expectedId));
    }
}