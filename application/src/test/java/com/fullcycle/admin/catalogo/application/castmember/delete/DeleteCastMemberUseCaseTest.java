package com.fullcycle.admin.catalogo.application.castmember.delete;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.fullcycle.admin.catalogo.application.Fixture;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;

public class DeleteCastMemberUseCaseTest extends UseCaseTest{

    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGatway;
    
    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGatway);
    }
    //__________________________________________________________________________

    @Test
    public void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt(){
        //givem
        final var member = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        final var expectedId = member.getId();

        Mockito.doNothing().when(castMemberGatway).deleteById(Mockito.any());

        //when
        Assertions.assertDoesNotThrow(()-> useCase.execute(expectedId.getValue()));

        //then
        Mockito.verify(castMemberGatway).deleteById(Mockito.eq(expectedId));
    }
    
    @Test
    public void givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk(){
        //givem
        final var expectedId = CastMemberID.from("123");

        Mockito.doNothing().when(castMemberGatway).deleteById(Mockito.any());

        //when
        Assertions.assertDoesNotThrow(()-> useCase.execute(expectedId.getValue()));

        //then
        Mockito.verify(castMemberGatway).deleteById(Mockito.eq(expectedId));
    }
    
    @Test
    public void givenAValidId_whenCallsDeleteCastMemberAndGatwayThrowsException_shouldReceiveException(){
        //givem
        final var member = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        final var expectedId = member.getId();

        Mockito.doThrow(new IllegalStateException("Gatway error")).when(castMemberGatway).deleteById(Mockito.any());

        //when
        Assertions.assertThrows(IllegalStateException.class, ()-> useCase.execute(expectedId.getValue()));

        //then
        Mockito.verify(castMemberGatway).deleteById(Mockito.eq(expectedId));
    }

}