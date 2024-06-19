package com.fullcycle.admin.catalogo.application.genre.create;

import java.util.List;

public record CreateGenreCommand(
    String name, 
    boolean active, 
    List<String> categories
) {
    public static CreateGenreCommand with(final String name, final Boolean active, final List<String> categories) {
        return new CreateGenreCommand(name, active != null ? active : true, categories);
    }
}
