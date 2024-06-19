package com.fullcycle.admin.catalogo.domain.castmember;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.entity.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;

public class CastMemberTest {

    @Test
    public void givenAValidParams_whenCallsNewMember_shouldInstatiateACastMember(){
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember(expectedName, expectedType);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallsNewMember_shouldReceiveNotification(){
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = Assertions
        .assertThrows(NotificationException.class, ()-> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
    
    @Test
    public void givenAnInvalidEmptyName_whenCallsNewMember_shouldReceiveNotification(){
        final var expectedName = "";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException = Assertions
        .assertThrows(NotificationException.class, ()-> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
    
    @Test
    public void givenAnInvalidNameWithLengthMore255_whenCallsNewMember_shouldReceiveNotification(){
        final String expectedName = 
            """
            Caros amigos, o julgamento imparcial das eventualidades maximiza as possibilidades por conta do impacto na agilidade decisória. Todavia,
            a complexidade dos estudos efetuados representa uma abertura para a melhoria do remanejamento dos quadros funcionais. Assim mesmo, o
            acompanhamento das preferências de consumo nos obriga à análise do sistema de participação geral. No entanto, não podemos esquecer que
            a estrutura atual da organização auxilia a preparação e a composição dos métodos utilizados na avaliação de resultados. Podemos já vislumbrar
            o modo pelo qual o novo modelo estrutural aqui preconizado garante a contribuição de um grupo importante na determinação das condições
            financeiras e administrativas exigidas.
            """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualException = Assertions
        .assertThrows(NotificationException.class, ()-> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
    
    @Test
    public void givenAnInvalidNullType_whenCallsNewMember_shouldReceiveNotification(){
        final var expectedName = "Vin Diesel";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualException = Assertions
        .assertThrows(NotificationException.class, ()-> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdate_shouldReceiveUpdated(){
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember("vini", CastMemberType.DIRECTOR);
        final var actualCreatedAt = actualMember.getCreatedAt();

        Assertions.assertNotNull(actualMember);
        
        actualMember.update(expectedName, expectedType);

        Assertions.assertNotNull(actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(actualCreatedAt, actualMember.getCreatedAt());
        //Assertions.assertTrue(actualUpdatedAt.isBefore(actualMember.getUpdatedAt()));
    }
    
    @Test
    public void givenAValidCastMember_whenCallsUpdateWithInvalidNullName_shouldReceiveNotification(){
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember("vini", CastMemberType.DIRECTOR);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = Assertions
        .assertThrows(NotificationException.class, ()-> actualMember.update(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
    
    @Test
    public void givenAValidCastMember_whenCallsUpdateWithInvalidEmptyName_shouldReceiveNotification(){
        final var expectedName = "";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember("vini", CastMemberType.DIRECTOR);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException = Assertions
        .assertThrows(NotificationException.class, ()-> actualMember.update(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
    
    @Test
    public void givenAValidCastMember_whenCallsUpdateWithLengthMore255_shouldReceiveNotification(){
        final var expectedName = """
            Caros amigos, o julgamento imparcial das eventualidades maximiza as possibilidades por conta do impacto na agilidade decisória. Todavia,
            a complexidade dos estudos efetuados representa uma abertura para a melhoria do remanejamento dos quadros funcionais. Assim mesmo, o
            acompanhamento das preferências de consumo nos obriga à análise do sistema de participação geral. No entanto, não podemos esquecer que
            a estrutura atual da organização auxilia a preparação e a composição dos métodos utilizados na avaliação de resultados. Podemos já vislumbrar
            o modo pelo qual o novo modelo estrutural aqui preconizado garante a contribuição de um grupo importante na determinação das condições
            financeiras e administrativas exigidas.
            """;
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember("vini", CastMemberType.DIRECTOR);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualException = Assertions
        .assertThrows(NotificationException.class, ()-> actualMember.update(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
    
    @Test
    public void givenAValidCastMember_whenCallsUpdateWithInvalidNullType_shouldReceiveNotification(){
        final var expectedName = "Vin Diesel";
        final CastMemberType expectedType = null;

        final var actualMember = CastMember.newMember("vini", CastMemberType.DIRECTOR);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualException = Assertions
        .assertThrows(NotificationException.class, ()-> actualMember.update(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}