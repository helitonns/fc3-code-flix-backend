package com.fullcycle.admin.catalogo.infrastructure.entity.genre;

import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import com.fullcycle.admin.catalogo.domain.entity.category.Category;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.entity.genre.Genre;
import com.fullcycle.admin.catalogo.domain.entity.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.entity.category.CategoryMySQLGateway;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.persistence.GenreJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.entity.genre.persistence.GenreRepository;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    //__________________________________________________________________________
    @Test
    public void givenAValidGenre_whenCallsCreateGenre_shouldPersistGenre(){
        final var filmes = categoryGateway.create(Category.newCategory("Fimes", null, true));

        final var expectedName = "Acao";
        final var expectedActive = true;
        final var expectedCategories = List.of(filmes.getId());

        final var genre = Genre.newGenre(expectedName, expectedActive);
        genre.addCategory(expectedCategories);

        Assertions.assertEquals(0, genreRepository.count());
        
        final var actualGenre = genreGateway.create(genre);
        
        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(genre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(genre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(genre.getId().getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        Assertions.assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertEquals(genre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), persistedGenre.getDeletedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }
    
    @Test
    public void givenAValidGenreWithoutCategories_whenCallsCreateGenre_shouldPersistGenre(){
        final var expectedName = "Acao";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre(expectedName, expectedActive);

        Assertions.assertEquals(0, genreRepository.count());
        
        final var actualGenre = genreGateway.create(genre);
        
        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(genre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(genre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(genre.getId().getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        Assertions.assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertEquals(genre.getUpdatedAt(), persistedGenre.getUpdatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), persistedGenre.getDeletedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }
    
    @Test
    public void givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldPersistGenre(){
        final var filmes = categoryGateway.create(Category.newCategory("Fimes", null, true));
        final var series = categoryGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "Acao";
        final var expectedActive = true;
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var genre = Genre.newGenre("ac", expectedActive);

        Assertions.assertEquals(0, genreRepository.count());
        
        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        
        Assertions.assertEquals("ac", genre.getName());
        Assertions.assertEquals(0, genre.getCategories().size());

        ;
        
        final var actualGenre = genreGateway.update(
            Genre.with(genre).update(expectedName, expectedActive, expectedCategories)
        );

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(genre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedActive, actualGenre.isActive());
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(genre.getId().getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedActive, persistedGenre.isActive());
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(persistedGenre.getCategoryIDs()));
        Assertions.assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertEquals(genre.getDeletedAt(), persistedGenre.getDeletedAt());
    }
    
    @Test
    public void givenAValidGenreWithCategories_whenCallsUpdateGenreCelaningCategories_shouldPersistGenre(){
        final var filmes = categoryGateway.create(Category.newCategory("Fimes", null, true));
        final var series = categoryGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "Acao";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre("ac", expectedActive);
        genre.addCategory(List.of(filmes.getId(), series.getId()));

        Assertions.assertEquals(0, genreRepository.count());
        
        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        
        Assertions.assertEquals("ac", genre.getName());
        Assertions.assertEquals(2, genre.getCategories().size());

        final var actualGenre = genreGateway.update(
            Genre.with(genre).update(expectedName, expectedActive, expectedCategories)
        );

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(genre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(genre.getId().getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        Assertions.assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertEquals(genre.getDeletedAt(), persistedGenre.getDeletedAt());
    }
    
    @Test
    public void givenAValidGenreInactive_whenCallsUpdateGenreActivating_shouldPersistGenre(){
        final var expectedName = "Acao";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre(expectedName, false);

        Assertions.assertEquals(0, genreRepository.count());
        
        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        
        Assertions.assertFalse(genre.isActive());
        Assertions.assertNotNull(genre.getDeletedAt());

        final var actualGenre = genreGateway.update(
            Genre.with(genre).update(expectedName, expectedActive, expectedCategories)
        );

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(genre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull( actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(genre.getId().getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        Assertions.assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    }
    
    @Test
    public void givenAValidGenreActive_whenCallsUpdateGenreInactivating_shouldPersistGenre(){
        final var expectedName = "Acao";
        final var expectedActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var genre = Genre.newGenre(expectedName, true);

        Assertions.assertEquals(0, genreRepository.count());
        
        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));
        
        Assertions.assertTrue(genre.isActive());
        Assertions.assertNull(genre.getDeletedAt());

        final var actualGenre = genreGateway.update(
            Genre.with(genre).update(expectedName, expectedActive, expectedCategories)
        );

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(genre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull( actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(genre.getId().getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        Assertions.assertEquals(genre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(genre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    }

    @Test
    public void givenAPrePersistedGenre_whenCallsDeleteById_shouldDeleteGenre(){
        //given
        final var genre = Genre.newGenre("Acao", true);
        
        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

        Assertions.assertEquals(1, genreRepository.count());
        
        //whem
        genreGateway.deleteById(genre.getId());
        
        //then
        Assertions.assertEquals(0, genreRepository.count());
    }    
    
    @Test
    public void givenAnInvalidGenre_whenCallsDeleteById_shouldReturnOK(){
        //given
        Assertions.assertEquals(0, genreRepository.count());
        
        //whem
        genreGateway.deleteById(GenreID.from("123"));
        
        //then
        Assertions.assertEquals(0, genreRepository.count());
    }    

    @Test
    public void givenPrePersistentedGenre_whenCallsFindById_shouldReturnGenre(){
        //given
        final var filmes = categoryGateway.create(Category.newCategory("Fimes", null, true));
        final var series = categoryGateway.create(Category.newCategory("Series", null, true));
        
        final var expectedName = "Acao";
        final var expectedActive = true;
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var genre = Genre.newGenre(expectedName, expectedActive);
        genre.addCategory(expectedCategories);

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

        Assertions.assertEquals(1, genreRepository.count());
        
        //whem
        final var actualGenre = genreGateway.findById(genre.getId()).get();
        
        //then
        Assertions.assertEquals(genre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedActive, actualGenre.isActive());
        Assertions.assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
        Assertions.assertEquals(genre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(genre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertEquals(genre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }
    
    @Test
    public void givenAnInvalidGenreId_whenCallsFindById_shouldReturnEmpty(){
        //given
        final var expectedId = GenreID.from("123");
        
        Assertions.assertEquals(0, genreRepository.count());
        
        //whem
        final var actualGenre = genreGateway.findById(expectedId);
        
        //then
        Assertions.assertTrue(actualGenre.isEmpty());
    }

    @Test
    public void givenEmptyGenres_whenCallsFindAll_shouldReturnEmpty(){
        //given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = genreGateway.findAll(query);

        //then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }
    
    @ParameterizedTest
    @CsvSource({
        "ac,0,10,1,1,Acao",
        "dr,0,10,1,1,Drama",
        "com,0,10,1,1,Comedia",
        "cien,0,10,1,1,Ficcao Cientifica",
        "terr,0,10,1,1,Terror"
    })
    public void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
        final String expectedTerms,
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final long expectedTotal,
        final String expectedGenreName
    ){
        //given
        mockGenres();
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = genreGateway.findAll(query);

        //then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
    }
    
    @ParameterizedTest
    @CsvSource({
        "name,asc,0,10,5,5,Acao",
        "name,desc,0,10,5,5,Terror",
        "createdAt,asc,0,10,5,5,Comedia",
        "createdAt,desc,0,10,5,5,Ficcao Cientifica"
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
        final String expectedSort,
        final String expectedDirection,
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final long expectedTotal,
        final String expectedGenreName
    ){
        //given
        mockGenres();
        final var expectedTerms = "";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = genreGateway.findAll(query);

        //then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
    }
    
    @ParameterizedTest
    @CsvSource({
        "0,2,2,5,Acao;Comedia",
        "1,2,2,5,Drama;Ficcao Cientifica",
        "2,2,1,5,Terror"
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final long expectedTotal,
        final String expectedGenres
    ){
        //given
        mockGenres();
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        //when
        final var actualPage = genreGateway.findAll(query);

        //then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for(final var expectedName : expectedGenres.split(";")){
            final var actualName = actualPage.items().get(index).getName();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }

    }
    
    private void mockGenres(){
        genreRepository.saveAndFlush(GenreJpaEntity.from(Genre.newGenre("Comedia", true)));
        genreRepository.saveAndFlush(GenreJpaEntity.from(Genre.newGenre("Acao", true)));
        genreRepository.saveAndFlush(GenreJpaEntity.from(Genre.newGenre("Drama", true)));
        genreRepository.saveAndFlush(GenreJpaEntity.from(Genre.newGenre("Terror", true)));
        genreRepository.saveAndFlush(GenreJpaEntity.from(Genre.newGenre("Ficcao Cientifica", true)));
    }

    private List<CategoryID> sorted(final List<CategoryID> categories){
        return categories.stream()
        .sorted(Comparator.comparing(CategoryID::getValue))
        .toList();
    }

    
}