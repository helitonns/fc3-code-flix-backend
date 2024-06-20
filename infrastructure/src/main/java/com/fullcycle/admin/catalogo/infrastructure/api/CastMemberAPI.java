package com.fullcycle.admin.catalogo.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.models.CastMemberResponse;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.models.CreateCastMemberRequest;
import com.fullcycle.admin.catalogo.infrastructure.entity.castmember.models.UpdateCastMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(value = "/cast_members")
@Tag(name = "Cast Members")
public interface CastMemberAPI {

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new cast member")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created successfuly"),
        @ApiResponse(responseCode = "422", description = "Unprocessable error"),
        @ApiResponse(responseCode = "500", description = "An internal server error was throw")
    })
    ResponseEntity<?> create(@RequestBody CreateCastMemberRequest input);
    //__________________________________________________________________________

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a cast member by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cast Member retrieved"),
        @ApiResponse(responseCode = "404", description = "Cast Member was not found"),
        @ApiResponse(responseCode = "500", description = "An internal server error was throw")
    })
    CastMemberResponse getById(@PathVariable String id);
    //__________________________________________________________________________
    
    @PutMapping(
        value = "{id}", 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a cast member by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cast Member updated"),
        @ApiResponse(responseCode = "404", description = "Cast Member was not found"),
        @ApiResponse(responseCode = "422", description = "A validation error was throw"),
        @ApiResponse(responseCode = "500", description = "An internal server error was throw")
    })
    ResponseEntity<?> updateById(@PathVariable String id, @RequestBody UpdateCastMemberRequest body);
    //__________________________________________________________________________

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a cast member by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cast Member deleted"),
        @ApiResponse(responseCode = "500", description = "An internal server error was throw")
    })
    void deleteById(@PathVariable String id);
    //__________________________________________________________________________
}