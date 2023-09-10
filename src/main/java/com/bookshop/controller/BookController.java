package com.bookshop.controller;

import com.bookshop.dto.book.BookDto;
import com.bookshop.dto.book.BookSearchParameters;
import com.bookshop.dto.book.CreateBookRequestDto;
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
    private static final String CODE_200 = "200";
    private static final String GET_LIST_DESCRIPTION = "List of books retrieved successfully";
    private static final String GET_BOOK_DESCRIPTION = "Book by id retrieved successfully";
    private static final String UPDATE_BOOK_DESCRIPTION = "Book updated successfully";
    private static final String CODE_201 = "201";
    private static final String CREATED_BOOK_DESCRIPTION = "Book created successfully";
    private static final String CODE_204 = "204";
    private static final String DELETED_BOOK_DESCRIPTION = "Book deleted successfully";
    private static final String CODE_400 = "400";
    private static final String CODE_400_DESCRIPTION = "Incorrect data was provided to the body";
    private static final String CODE_400_EXAMPLE =
            """
            {
                "timestamp": "*time of query*",
                "status": "BAD_REQUEST",
                "errors": [
                    "title can't be null, should be set"
                ]
            }
            """;
    private static final String CODE_404 = "404";
    private static final String CODE_404_DESCRIPTION =
            "Book with such id doesn't exists or was previously deleted";
    private static final String CODE_404_EXAMPLE =
            """          
                {
                    "timestamp": "*time of query*",
                    "status": "NOT_FOUND",
                    "errors": "Can't find book by id: {id}"
                }
            """;
    private static final String CODE_500 = "500";
    private static final String CODE_500_DESCRIPTION = "Internal server error";
    private static final String CODE_500_EXAMPLE =
            """
                {
                    "timestamp": "*time of query*",
                    "status": "BAD_REQUEST",
                    "errors": [
                        "*problem description*"
                    ]
                }
            """;
    private static final String MEDIA_TYPE = "application/json";
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get list of all books",
            description = "Returns a list of books based on the provided paging information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_200, description = GET_LIST_DESCRIPTION),
        @ApiResponse(responseCode = CODE_500, description = CODE_500_DESCRIPTION,
            content = {@Content(mediaType = MEDIA_TYPE,
                    examples = {@ExampleObject(value = CODE_500_EXAMPLE)}
                    )}
            )
    })
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by id", description = "Return book with certain id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_200, description = GET_BOOK_DESCRIPTION),
        @ApiResponse(responseCode = CODE_404, description = CODE_404_DESCRIPTION,
            content = {@Content(mediaType = MEDIA_TYPE,
                    examples = {@ExampleObject(value = CODE_404_EXAMPLE)}
                    )}
            ),
        @ApiResponse(responseCode = CODE_500, description = CODE_500_DESCRIPTION,
            content = {@Content(mediaType = MEDIA_TYPE,
                    examples = {@ExampleObject(value = CODE_500_EXAMPLE)}
                    )}
            ),
    })
    public BookDto getById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new book",
            description = "Creates a new book based on data, provided in the body")
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_201, description = CREATED_BOOK_DESCRIPTION),
        @ApiResponse(responseCode = CODE_400, description = CODE_400_DESCRIPTION,
            content = {@Content(mediaType = MEDIA_TYPE,
                    examples = {@ExampleObject(value = CODE_400_EXAMPLE)}
                    )}
            ),
        @ApiResponse(responseCode = CODE_500, description = CODE_500_DESCRIPTION,
            content = {@Content(mediaType = MEDIA_TYPE,
                    examples = {@ExampleObject(value = CODE_500_EXAMPLE)}
                    )}
            ),
    })
    public BookDto create(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book by id",
                description = "Updates a book with certain id, based on data, provided in the body")
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_200, description = UPDATE_BOOK_DESCRIPTION),
        @ApiResponse(responseCode = CODE_400, description = CODE_400_DESCRIPTION,
            content = {@Content(mediaType = MEDIA_TYPE,
                    examples = {@ExampleObject(value = CODE_400_EXAMPLE)}
                    )}
            ),
        @ApiResponse(responseCode = CODE_500, description = CODE_500_DESCRIPTION,
            content = {@Content(mediaType = MEDIA_TYPE,
                    examples = {@ExampleObject(value = CODE_500_EXAMPLE)}
                    )}
            ),
    })
    public BookDto update(@PathVariable Long id,
                                  @RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a book by id", description = "Deletes a book with certain id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_204, description = DELETED_BOOK_DESCRIPTION),
        @ApiResponse(responseCode = CODE_404, description = CODE_404_DESCRIPTION,
            content = {@Content(mediaType = MEDIA_TYPE,
                    examples = {@ExampleObject(value = CODE_404_EXAMPLE)}
                    )}
            ),
        @ApiResponse(responseCode = CODE_500, description = CODE_500_DESCRIPTION,
            content = {@Content(mediaType = MEDIA_TYPE,
                    examples = {@ExampleObject(value = CODE_500_EXAMPLE)}
                    )}
            ),
    })
    public void deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Get books with params",
            description = "Returns a list of books that match the specified parameters "
                    + "received in the request body, based on the provided paging information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = CODE_200, description = GET_LIST_DESCRIPTION),
        @ApiResponse(responseCode = CODE_400, description = CODE_400_DESCRIPTION,
            content = {@Content(mediaType = MEDIA_TYPE,
                    examples = {@ExampleObject(value = CODE_400_EXAMPLE)}
                    )}
            ),
        @ApiResponse(responseCode = CODE_500, description = CODE_500_DESCRIPTION,
            content = {@Content(mediaType = MEDIA_TYPE,
                    examples = {@ExampleObject(value = CODE_500_EXAMPLE)}
                    )}
            ),
    })
    public List<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable) {
        return bookService.search(bookSearchParameters, pageable);
    }
}
