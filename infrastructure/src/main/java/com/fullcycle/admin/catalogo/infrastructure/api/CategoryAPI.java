package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.entity.category.models.CategoryListResponse;
import com.fullcycle.admin.catalogo.infrastructure.entity.category.models.CategoryResponse;
import com.fullcycle.admin.catalogo.infrastructure.entity.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.entity.category.models.UpdateCategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/categories")
@Tag(name = "Categories")
public interface CategoryAPI {

        @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Create a new category")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "Created successfully"),
                @ApiResponse(responseCode = "422", description = "Unprocessable error"),
                @ApiResponse(responseCode = "500", description = "An internal server error was throw"),
        })
        ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest input);
        // --------------------------------------------------------------------------

        @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "List all categories paginated")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Listed successfully"),
                @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
                @ApiResponse(responseCode = "500", description = "An internal server error was throw"),
        })
        Pagination<CategoryListResponse> listCategories(
                @RequestParam(name = "search", required = false, defaultValue = "") final String search,
                @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
                @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
                @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
                @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
        );
        // --------------------------------------------------------------------------


        @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Get a category by it's identifier")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
                @ApiResponse(responseCode = "404", description = "Category was not found"),
                @ApiResponse(responseCode = "500", description = "An internal server error was throw")
        })
        CategoryResponse getById(@PathVariable(name = "id") String id);
        // --------------------------------------------------------------------------


        @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        @Operation(summary = "Update a category by it's identifier")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Category updated successfully"),
                @ApiResponse(responseCode = "404", description = "Category was not found"),
                @ApiResponse(responseCode = "500", description = "An internal server error was throw")
        })
        ResponseEntity<?> updateById(@PathVariable(name = "id") String id, @RequestBody UpdateCategoryRequest input);
        // --------------------------------------------------------------------------


        @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
        @ResponseStatus(HttpStatus.NO_CONTENT)
        @Operation(summary = "Delete a category by it's identifier")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
                @ApiResponse(responseCode = "404", description = "Category was not found"),
                @ApiResponse(responseCode = "500", description = "An internal server error was throw")
        })
        void deleteById(@PathVariable(name = "id") String id);
        // --------------------------------------------------------------------------

}
