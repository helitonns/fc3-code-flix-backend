package com.fullcycle.admin.catalogo.application.castmember.retrieve.list;

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
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;

public class ListCastMemberByIdUseCaseTest extends UseCaseTest {
    
    @InjectMocks
    private DefaultListCastMembersUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGatway;
    
    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGatway);
    }
    //__________________________________________________________________________

    @Test
    public void givenAValidQuery_whenCallsListCastMembers_shouldReturnAll(){
        //given
        final var members = List.of(
            CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()),
            CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdeAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = members.stream().map(CastMemberListOutput::from).toList();

        final var expectedPagination = new Pagination<>(
            expectedPage, 
            expectedPerPage, 
            expectedTotal, 
            members
        );

        Mockito.when(castMemberGatway.findAll(Mockito.any())).thenReturn(expectedPagination);

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualOutput = useCase.execute(query);

        //then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(castMemberGatway).findAll(Mockito.eq(query));
    }
    
    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndIsEmnpty_shouldReturn(){
        //given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var members = List.<CastMember>of();
        final var expectedItems = List.<CastMemberListOutput>of();

        final var expectedPagination = new Pagination<>(
            expectedPage, 
            expectedPerPage, 
            expectedTotal, 
            members
        );

        Mockito.when(castMemberGatway.findAll(Mockito.any())).thenReturn(expectedPagination);

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualOutput = useCase.execute(query);

        //then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(castMemberGatway).findAll(Mockito.eq(query));
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndGatewayThrowsRandomException_shouldException(){
        //given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        Mockito.when(castMemberGatway.findAll(Mockito.any())).thenThrow(new IllegalStateException("Gateway error"));

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualException = Assertions.assertThrows(IllegalStateException.class, ()-> useCase.execute(query));

        //then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(castMemberGatway).findAll(Mockito.eq(query));
    }


}