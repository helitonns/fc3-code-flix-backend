package com.fullcycle.admin.catalogo.application.castmember.retrieve.get;

import java.util.List;
import java.util.Optional;
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
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;

public class GetCastMemberByIdUseCaseIT extends UseCaseTest {
    @InjectMocks
    private DefaultGetCastMemberByIdUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGatway;
    
    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGatway);
    }
    //__________________________________________________________________________

    @Test
    public void givenAValidId_whenCallsGetCastMember_shouldReturnIt(){
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();

        Mockito.when(castMemberGatway.findById(Mockito.any())).thenReturn(Optional.of(member));
        
        //when
        final var actualOutput = useCase.execute(expectedId.getValue());

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedType, actualOutput.type());
        Assertions.assertEquals(member.getCreatedAt(), actualOutput.createdAt());
        Assertions.assertEquals(member.getUpdatedAt(), actualOutput.updatedAt());

        Mockito.verify(castMemberGatway).findById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFoundException(){
        //given
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        Mockito.when(castMemberGatway.findById(Mockito.any())).thenReturn(Optional.empty());
        
        //when
        final var actualOutput = Assertions.assertThrows(NotFoundException.class, ()-> useCase.execute(expectedId.getValue()));

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

        Mockito.verify(castMemberGatway).findById(Mockito.eq(expectedId));
    }
}