package com.fullcycle.admin.catalogo.application.castmember.delete;

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
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.persistence.CastMemberRepository;

@IntegrationTest
public class DeleteCastMemberUseCaseIT {

    @Autowired
    private DeleteCastMemberUseCase useCase;
    
    @Autowired
    private CastMemberRepository repository;

    @SpyBean
    private CastMemberGateway gateway;
    
    //__________________________________________________________________________

    @Test
    public void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt(){
        //givem
        final var member = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var expectedId = member.getId();

        this.repository.saveAndFlush(CastMemberJpaEntity.from(member));
        
        Assertions.assertEquals(1, this.repository.count());

        //when
        Assertions.assertDoesNotThrow(()-> useCase.execute(expectedId.getValue()));

        //then
        Mockito.verify(gateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertEquals(0, this.repository.count());
    }
    
    @Test
    public void givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk(){
        //givem
        final var expectedId = CastMemberID.from("123");

        //when
        Assertions.assertDoesNotThrow(()-> useCase.execute(expectedId.getValue()));

        //then
        Mockito.verify(gateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertEquals(0, this.repository.count());
    }
    
    @Test
    public void givenAValidId_whenCallsDeleteCastMemberAndGatwayThrowsException_shouldReceiveException(){
        //givem
        final var member = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var expectedId = member.getId();

        Mockito.doThrow(new IllegalStateException("Gatway error")).when(gateway).deleteById(Mockito.any());

        //when
        Assertions.assertThrows(IllegalStateException.class, ()-> useCase.execute(expectedId.getValue()));

        //then
        Mockito.verify(gateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertEquals(0, this.repository.count());
    }

}