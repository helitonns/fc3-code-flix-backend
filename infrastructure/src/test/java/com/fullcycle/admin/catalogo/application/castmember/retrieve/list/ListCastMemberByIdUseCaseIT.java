package com.fullcycle.admin.catalogo.application.castmember.retrieve.list;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import com.fullcycle.admin.catalogo.Fixture;
import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.persistence.CastMemberRepository;

@IntegrationTest
public class ListCastMemberByIdUseCaseIT {
    
    @Autowired
    private ListCastMembersUseCase useCase;
    
    @Autowired
    private CastMemberRepository repository;

    @SpyBean
    private CastMemberGateway gateway;
    //__________________________________________________________________________

    @Test
    public void givenAValidQuery_whenCallsListCastMembers_shouldReturnAll(){
        //given
        final var members = List.of(
            CastMember.newMember(Fixture.name(), Fixture.CastMember.type()),
            CastMember.newMember(Fixture.name(), Fixture.CastMember.type())
        );

        repository.saveAllAndFlush(members.stream().map(CastMemberJpaEntity::from).toList());

        Assertions.assertEquals(2, repository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        //final var expectedItems = members.stream().map(CastMemberListOutput::from).toList();

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualOutput = useCase.execute(query);

        //then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        // Assertions.assertTrue(
        //     expectedItems.size() == actualOutput.items().size()
        //     && expectedItems.containsAll(actualOutput.items())
        // );

        Mockito.verify(gateway).findAll(Mockito.any());
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

        final var expectedItems = List.<CastMemberListOutput>of();

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        Assertions.assertEquals(0, repository.count());

        //when
        final var actualOutput = useCase.execute(query);

        //then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(gateway).findAll(Mockito.any());
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndGatewayThrowsRandomException_shouldException(){
        //given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage)).when(gateway).findAll(Mockito.any());
            
        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualException = Assertions.assertThrows(IllegalStateException.class, ()-> useCase.execute(query));

        //then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(gateway).findAll(Mockito.any());
    }


}