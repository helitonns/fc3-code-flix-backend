package com.fullcycle.admin.catalogo.infrastructure.entity.castmember.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import com.fullcycle.admin.catalogo.Fixture;
import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.CastMemberMySQLGateway;

@MySQLGatewayTest
public class CastMemberRepositoryTest {

    @Autowired
    private CastMemberMySQLGateway gateway;

    @Autowired
    private CastMemberRepository repository;

    @Test
    public void givenAValidCastMember_whenCallsCreate_shouldPersistIt(){
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var member = CastMember.newMember(expectedName, expectedType);
        final var expectedId = member.getId();

        Assertions.assertEquals(0, repository.count());

        //when
        final var actualMember = gateway.create(CastMember.with(member));

        //then
        Assertions.assertEquals(1, repository.count());
        
        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(member.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertEquals(member.getUpdatedAt(), actualMember.getUpdatedAt());

        final var persistedMember = repository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), persistedMember.getId());
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(member.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertEquals(member.getUpdatedAt(), persistedMember.getUpdatedAt());
    }
    
    @Test
    public void givenAValidCastMember_whenCallsUpdate_shouldRefreshIt(){
        //given
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;
        final var member = CastMember.newMember("vin", CastMemberType.DIRECTOR);
        final var expectedId = member.getId();

        final var currentMember = repository.saveAndFlush(CastMemberJpaEntity.from(member));
        
        Assertions.assertEquals(1, repository.count());
        Assertions.assertEquals("vin", currentMember.getName());
        Assertions.assertEquals(CastMemberType.DIRECTOR, currentMember.getType());

        //when
        final var actualMember = gateway.update(CastMember.with(member).update(expectedName, expectedType));

        //then
        Assertions.assertEquals(1, repository.count());
        
        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(member.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertTrue(member.getUpdatedAt().isBefore(actualMember.getUpdatedAt()));

        final var persistedMember = repository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), persistedMember.getId());
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(member.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertTrue(member.getUpdatedAt().isBefore(persistedMember.getUpdatedAt()));
    }

    @Test
    public void givenAValidCastMember_whenCallDeleteById_shouldDeleteIt(){
        //given
        final var member = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        repository.saveAndFlush(CastMemberJpaEntity.from(member));
        Assertions.assertEquals(1, repository.count());
        
        //when
        gateway.deleteById(member.getId());

        //then
        Assertions.assertEquals(0, repository.count());
    }

    @Test
    public void givenAnInvalidCastMember_whenCallDeleteById_shouldBeIgnored(){
        //given
        final var member = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        repository.saveAndFlush(CastMemberJpaEntity.from(member));
        Assertions.assertEquals(1, repository.count());
        
        //when
        gateway.deleteById(CastMemberID.from("123"));

        //then
        Assertions.assertEquals(1, repository.count());
    }

    @Test
    public void givenAValidCastMember_whenCallsFindById_shouldReturnIt(){
        //given
        final var member = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        final var expectedId = member.getId();
        final var expectedName = member.getName();
        final var expectedType = member.getType();

        repository.saveAndFlush(CastMemberJpaEntity.from(member));
        Assertions.assertEquals(1, repository.count());
        
        //when
        final var actualMember = gateway.findById(expectedId).get();

        //then
        Assertions.assertEquals(1, repository.count());

        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(member.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertEquals(member.getUpdatedAt(), actualMember.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidId_whenCallsFindById_shouldReturnEmpty(){
        //given
        final var member = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        repository.saveAndFlush(CastMemberJpaEntity.from(member));

        Assertions.assertEquals(1, repository.count());
        
        //when
        final var actualMember = gateway.findById(CastMemberID.from("123"));

        //then
        Assertions.assertTrue(actualMember.isEmpty());
    }

    @Test
    public void givenEmptyCastMembers_whenCallsFindAll_shouldReturnEmpty(){
        //given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = gateway.findAll(query);

        //then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
        "vin,0,10,1,1,Vin Diesel",
        "taran,0,10,1,1,Quentin Tarantino",
        "jas,0,10,1,1,Jason Momoa",
        "har,0,10,1,1,Kit harington",
        "MAR,0,10,1,1,Martin Scorsese"
    })
    public void givenAValidTerms_whenCallsFindAll_shouldReturnFilterd(
        final String expectedTerms,
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final long expectedTotal,
        final String expectedName
    ){
        //givem
        mockMember();
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = gateway.findAll(query);

        //then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
        "name,asc,0,10,5,5,Jason Momoa",
        "name,desc,0,10,5,5,Vin Diesel",
        "createdAt,asc,0,10,5,5,Kit harington",
        "createdAt,desc,0,10,5,5,Martin Scorsese"
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnSorted(
        final String expectedSort,
        final String expectedDirection,
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final long expectedTotal,
        final String expectedName
    ){
        //givem
        mockMember();
        final var expectedTerms = "";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = gateway.findAll(query);

        //then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
        "0,2,2,5,Jason Momoa;Kit harington",
        "1,2,2,5,Martin Scorsese;Quentin Tarantino",
        "2,2,1,5,Vin Diesel"
    })
    public void givenAValidPagination_whenCallsFindAll_shouldReturnPaginated(
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final long expectedTotal,
        final String expectedName
    ){
        //givem
        mockMember();
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = gateway.findAll(query);

        //then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for(final var name: expectedName.split(";")){
            Assertions.assertEquals(name, actualPage.items().get(index).getName());
            index++;
        }
    }

    //__________________________________________________________________________
    private void mockMember(){
        repository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Kit harington", CastMemberType.ACTOR)));
        repository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Vin Diesel", CastMemberType.ACTOR)));
        repository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Quentin Tarantino", CastMemberType.DIRECTOR)));
        repository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Jason Momoa", CastMemberType.ACTOR)));
        repository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Martin Scorsese", CastMemberType.DIRECTOR)));
    }
}