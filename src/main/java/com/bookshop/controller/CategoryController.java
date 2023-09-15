package com.bookshop.controller;

import com.bookshop.dto.book.BookDtoWithoutCategoryIds;
import com.bookshop.dto.category.CategoryDto;
import com.bookshop.res.Openapi;
import com.bookshop.service.BookService;
import com.bookshop.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category", description = "Operations related to book categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private static final String BAD_REQUEST_EXAMPLE = Openapi.BAD_REQUEST_EXAMPLE;
    private static final String CATEGORY_NOT_FOUND_EXAMPLE = Openapi.OBJECT_NOT_FOUND_EXAMPLE;

    private final CategoryService categoryService;
    private final BookService bookService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Incorrect data was provided to the body",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do this operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "403",
                    description = "Only users with role \"MANAGER\" can do such operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
    })
    @Operation(summary = "Create category",
            description = "Creates a category based on data, provided in the body")
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List of categories retrieved successfully"),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do this operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            )
    })
    @GetMapping
    @Operation(summary = "Get list of all categories",
            description = "Returns a list of categories based on the provided paging information")
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Category by id retrieved successfully"),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do this operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "404",
                    description = "Category with such id doesn't exists or was previously deleted",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = CATEGORY_NOT_FOUND_EXAMPLE)}
                            )}
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
    })
    @GetMapping("/{id}")
    @Operation(summary = "Get category by id", description = "Returns a category with certain id")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a category by id",
            description = "Updates a category with certain id, based on data, provided in the body")
    public CategoryDto updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do this operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "403",
                    description = "Only users with role \"MANAGER\" can do such operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "404",
                    description = "Category with such id doesn't exists or was previously deleted",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = CATEGORY_NOT_FOUND_EXAMPLE)}
                            )}
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            ),
    })
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category by id",
            description = "Deletes a category with certain id")
    @PreAuthorize("hasRole('MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List of books with category retrieved successfully"),
            @ApiResponse(responseCode = "401",
                    description = "User should be authenticated to do this operation",
                    content = {@Content()}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                            )}
            )
    })
    @GetMapping("/{id}/books")
    @Operation(summary = "Get list of all books which contain category with certain id",
            description = "Returns a list of books based on category and the paging information")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @PathVariable Long id, Pageable pageable) {
        return bookService.getBooksByCategoryId(id, pageable);
    }
}
