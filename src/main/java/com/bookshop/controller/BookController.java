package com.bookshop.controller;

import com.bookshop.dto.book.request.BookSearchParameters;
import com.bookshop.dto.book.request.CreateBookRequestDto;
import com.bookshop.dto.book.response.BookDto;
import com.bookshop.res.Openapi;
import com.bookshop.service.BookService;
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

@Tag(name = "Books", description = "Operations related to books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private static final String BAD_REQUEST_EXAMPLE = Openapi.BAD_REQUEST_EXAMPLE;

    private static final String BOOK_NOT_FOUND_EXAMPLE = Openapi.OBJECT_NOT_FOUND_EXAMPLE;

    private final BookService bookService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of books retrieved successfully"),
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
    @Operation(summary = "Get list of all books",
            description = "Returns a list of books based on the provided paging information")
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book by id retrieved successfully"),
        @ApiResponse(responseCode = "401",
            description = "User should be authenticated to do this operation",
            content = {@Content()}),
        @ApiResponse(responseCode = "404",
            description = "Book with such id doesn't exists or was previously deleted",
            content = {@Content(mediaType = "application/json",
                    examples = {@ExampleObject(value = BOOK_NOT_FOUND_EXAMPLE)}
                    )}
            ),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = {@Content(mediaType = "application/json",
                    examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                    )}
            ),
    })
    @GetMapping("/{id}")
    @Operation(summary = "Get book by id", description = "Returns a book with certain id")
    public BookDto getById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Book created successfully"),
        @ApiResponse(responseCode = "400", description = "Incorrect data was provided to the body",
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
    @Operation(summary = "Create a new book",
            description = "Creates a new book based on data, provided in the body")
    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book updated successfully"),
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
    @Operation(summary = "Update a book by id",
            description = "Updates a book with certain id, based on data, provided in the body")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping("/{id}")
    public BookDto update(@PathVariable Long id,
                                  @RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.update(id, requestDto);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
        @ApiResponse(responseCode = "401",
            description = "User should be authenticated to do this operation",
            content = {@Content()}),
        @ApiResponse(responseCode = "403",
            description = "Only users with role \"MANAGER\" can do such operation",
            content = {@Content()}),
        @ApiResponse(responseCode = "404",
            description = "Book with such id doesn't exists or was previously deleted",
            content = {@Content(mediaType = "application/json",
                    examples = {@ExampleObject(value = BOOK_NOT_FOUND_EXAMPLE)}
                    )}
            ),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = {@Content(mediaType = "application/json",
                    examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                    )}
            ),
    })
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book by id", description = "Deletes a book with certain id")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "List of books retrieved successfully"),
        @ApiResponse(responseCode = "400",
            description = "Incorrect data was provided to the body",
            content = {@Content(mediaType = "application/json",
                    examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                    )}
            ),
        @ApiResponse(responseCode = "401",
            description = "User should be authenticated to do this operation",
            content = {@Content()}),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = {@Content(mediaType = "application/json",
                    examples = {@ExampleObject(value = BAD_REQUEST_EXAMPLE)}
                    )}
            ),
    })
    @GetMapping("/search")
    @Operation(summary = "Get books with params",
            description = "Returns a list of books that match the specified parameters "
                    + "received in the request body, based on the provided paging information")
    public List<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable) {
        return bookService.search(bookSearchParameters, pageable);
    }
}
