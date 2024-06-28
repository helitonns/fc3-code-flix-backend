package com.fullcycle.admin.catalogo.application;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.List;
import static io.vavr.API.Match;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.entity.category.Category;
import com.fullcycle.admin.catalogo.domain.entity.genre.Genre;
import com.fullcycle.admin.catalogo.domain.entity.video.Rating;
import com.fullcycle.admin.catalogo.domain.entity.video.Resource;
import com.github.javafaker.Faker;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name(){
        return FAKER.name().fullName();
    }
    
    public static String title(){
        return FAKER.options().option("Naruto", "One Piece", "Bleach", "Full Metal Achimist");
    }

    public static Integer year(){
        return FAKER.random().nextInt(2020, 2030);
    }
    
    public static Double duration(){
        return FAKER.options().option(120.0, 15.5, 35.0, 10.0, 2.0);
    }
    
    public static boolean bool(){
        return FAKER.bool().bool();
    }


    //__________________________________________________________________________
    public static final class Categories {
        private static final Category AULAS = Category.newCategory("Aulas", "Some description", true);

        public static Category aulas(){
            return Category.with(AULAS);
        }
    }
    
    public static final class Genres {
        private static final Genre TECH = Genre.newGenre("Tech", true);

        public static Genre tech(){
            return Genre.with(TECH);
        }
    }

    public static final class CastMembers {
        private static final CastMember WESLEY = CastMember.newMember("Wesley Fullcycle", CastMemberType.ACTOR);
        private static final CastMember HELITON = CastMember.newMember("Heliton", CastMemberType.DIRECTOR);

        public static CastMemberType type(){
            return FAKER.options().option(CastMemberType.ACTOR, CastMemberType.DIRECTOR);
        }

        public static CastMember wesley(){
            return CastMember.with(WESLEY);
        }
        
        public static CastMember heliton(){
            return CastMember.with(HELITON);
        }
    }
    
    public static final class Videos {
        public static String description(){
            return FAKER.options().option("Descrição 11111","Descrição 22222","Descrição 33333","Descrição 44444");
        }

        public static Rating rating(){
            return FAKER.options().option(Rating.values());
        }

        public static Resource resource(final Resource.Type type){
            final String contentType = Match(type).of(
                Case($(List(Resource.Type.VIDEO, Resource.Type.TRAILER)::contains), "video/mp4"),
                Case($(), "image/jpg")
            );

            final byte[] content = "Conteudo".getBytes();

            return Resource.with(content, contentType, type.name().toLowerCase(), type);
        }

    }
}