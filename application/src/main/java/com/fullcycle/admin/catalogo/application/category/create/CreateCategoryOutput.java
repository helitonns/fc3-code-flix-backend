package com.fullcycle.admin.catalogo.application.category.create;

import com.fullcycle.admin.catalogo.domain.entity.category.Category;

public record CreateCategoryOutput(
        String id
) {
    public static CreateCategoryOutput from(final Category aCategory){
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }

    public static CreateCategoryOutput from(final String id){
        return new CreateCategoryOutput(id);
    }
}
