package com.fullcycle.admin.catalogo.application.category.retrieve.get;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.entity.category.Category;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;

public class GetCategoryByIdUseCaseTest extends UseCaseTest {

    @Mock
    private CategoryGateway categoryGateway;

    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }
    //__________________________________________________________________________

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory(){
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectId = aCategory.getId();

        Mockito.when(categoryGateway.findById(Mockito.eq(expectId))).thenReturn(Optional.of(aCategory.clone()));

        final var actualCategory = useCase.execute(expectId.getValue());

        Assertions.assertEquals(CategoryOutput.from(aCategory), actualCategory);

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(expectId));
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound(){
        final var expectId = CategoryID.from("123");
        final var expectedMessage = "Category with ID 123 was not found";

        Mockito.when(categoryGateway.findById(Mockito.eq(expectId))).thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(expectId.getValue()));

        Assertions.assertEquals(expectedMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(expectId));
    }

    @Test
    public void givenAValidId_whenGatewayThrowsError_shouldReturnException(){
        final var expectId = CategoryID.from("123");
        final var expectedMessage = "Gateway error";

        Mockito.when(categoryGateway.findById(Mockito.eq(expectId))).thenThrow(new IllegalStateException(new Error(expectedMessage)));

        Assertions.assertThrows(IllegalStateException.class, ()-> useCase.execute(expectId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(expectId));
    }
}

















