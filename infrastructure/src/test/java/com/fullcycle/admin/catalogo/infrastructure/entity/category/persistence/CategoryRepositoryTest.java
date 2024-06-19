package com.fullcycle.admin.catalogo.infrastructure.entity.category.persistence;

import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import com.fullcycle.admin.catalogo.domain.entity.category.Category;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Test
    public void giveAnInvalidNullName_whenCallsSave_shouldReturnsError(){
        final var expectedPropertyName = "name";
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var entity = CategoryJpaEntity.from(category);

        entity.setName(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
    }

    @Test
    public void giveAnInvalidNullCreatedAt_whenCallsSave_shouldReturnsError(){
        final var expectedPropertyName = "createdAt";
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var entity = CategoryJpaEntity.from(category);

        entity.setCreatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
    }

    @Test
    public void giveAnInvalidNullUpdatedAt_whenCallsSave_shouldReturnsError(){
        final var expectedPropertyName = "updatedAt";
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var entity = CategoryJpaEntity.from(category);

        entity.setUpdatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
    }

}
