package com.fullcycle.admin.catalogo.domain.entity.video;

public record VideoSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}
