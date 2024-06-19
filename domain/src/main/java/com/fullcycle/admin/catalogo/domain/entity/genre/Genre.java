package com.fullcycle.admin.catalogo.domain.entity.genre;

import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.entity.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genre extends AggregateRoot<GenreID> {
    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Genre(final GenreID genreID, final String name, final boolean active, final List<CategoryID> categories, final Instant createdAt, final Instant updatedAt, final Instant deletedAt) {
        super(genreID);
        this.name = name;
        this.active = active;
        this.categories = categories;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;

        selfValidate();
    }

    public static Genre newGenre(final String name, final boolean active){
        final var id = GenreID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = active ? null : now;

        return new Genre(id, name, active, new ArrayList<>(), now, now, deletedAt);
    }

    public static Genre with(final GenreID genreID, final String name, final boolean active, final List<CategoryID> categories, final Instant createdAt, final Instant updatedAt, final Instant deletedAt) {
        return new Genre(genreID, name, active, categories, createdAt, updatedAt, deletedAt);
    }

    public static Genre with(final Genre genre) {
        return new Genre(genre.id, genre.name, genre.active, new ArrayList<>(genre.categories), genre.createdAt, genre.updatedAt, genre.deletedAt);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public Genre update(final String name, final boolean active, final List<CategoryID> categories){
        if (active){
            activate();
        }else{
            deactivate();
        }

        this.name = name;
        this.categories = new ArrayList<>(categories != null ? categories : Collections.emptyList());
        this.updatedAt = InstantUtils.now();

        selfValidate();

        return this;
    }

    public Genre deactivate(){
        if(this.deletedAt == null){
            this.deletedAt = InstantUtils.now();
        }

        this.active = false;
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Genre activate(){
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre addCategory(final CategoryID categoryID){
        if (categoryID == null){
            return this;
        }
        this.categories.add(categoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre addCategory(final List<CategoryID> categories){
        if (categories == null || categories.isEmpty()){
            return this;
        }
        this.categories.addAll(categories);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre removeCategory(final CategoryID categoryID){
        if (categoryID == null){
            return this;
        }
        this.categories.remove(categoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()){
            throw new NotificationException("Failed to create a validate Aggregate Genre", notification);
        }
    }
    //__________________________________________________________________________________________________________________
    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

}
