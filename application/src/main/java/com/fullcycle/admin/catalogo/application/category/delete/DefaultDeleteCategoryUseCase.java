package com.fullcycle.admin.catalogo.application.category.delete;

import java.util.Objects;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }


    @Override
    public void execute(final String anIn) {
        this.categoryGateway.deleteById(CategoryID.from(anIn));
    }
}
