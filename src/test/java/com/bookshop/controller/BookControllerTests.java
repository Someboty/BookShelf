package com.bookshop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookshop.dto.book.request.CreateBookRequestDto;
import com.bookshop.dto.book.response.BookDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTests {
    protected static MockMvc mockMvc;
    private static final String REMOVE_ALL_BOOKS_AND_CATEGORIES =
            "classpath:database/books/remove-all-books-and-categories.sql";
    private static final String ADD_ONE_CATEGORY = "classpath:database/books/add-one-category.sql";
    private static final String ADD_ONE_BOOK_WITH_CATEGORY =
            "classpath:database/books/add-one-book-with-first-category.sql";
    private static final String ACCESS_DENIED_MESSAGE = "Access Denied";
    private static final String TEST_MANAGER_CREDENTIALS = "admin";
    private static final String TEST_MANAGER_ROLE = "MANAGER";
    private static final String TEST_USER_CREDENTIALS = "user";
    private static final String SHORT_ISBN = "1";
    private static final String INVALID_ISBN = "qwertyuiop";
    private static final Pageable STANDART_PAGEABLE = PageRequest.of(0, 20);
    private static final Long CORRECT_ID_ONE = 1L;
    private static final Long CORRECT_ID_TWO = 2L;
    private static final Long CORRECT_ID_THREE = 3L;
    private static final int NEGATIVE_PRICE = -2;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Test
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new book with all valid fields by manager")
    public void create_ValidRequestDtoWithAllFieldsByManager_ReturnsCorrectDto() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        BookDto expected = mapCreateDtoToDto(requestDto);
        
        MvcResult result = mockMvc.perform(post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = TEST_USER_CREDENTIALS)
    @Test
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Try to create a new book with all valid fields by user")
    public void create_ValidRequestDtoWithAllFieldsByUser_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        String actual = Objects.requireNonNull(result.getResolvedException()).getMessage();
        assertEquals(ACCESS_DENIED_MESSAGE, actual);
    }

    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Test
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new book with only necessary fields by manager")
    public void create_ValidRequestDtoWithOnlyNecessaryFieldsByManager_ReturnsCorrectDto()
            throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        requestDto.setCoverImage(null);
        requestDto.setDescription(null);
        requestDto.setCategoryIds(new HashSet<>());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        BookDto expected = mapCreateDtoToDto(requestDto);
        
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Test
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new book without title by manager")
    public void create_DtoWithoutTitleByManager_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        requestDto.setTitle(null);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        
        assertTrue(Objects.requireNonNull(
                result.getResolvedException()).getMessage()
                .contains("title can't be null, should be set"));
    }

    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Test
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new book without author by manager")
    public void create_DtoWithoutAuthorByManager_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        requestDto.setAuthor(null);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        
        assertTrue(Objects.requireNonNull(
                        result.getResolvedException()).getMessage()
                .contains("author can't be null, should be set"));
    }

    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Test
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new book without price by manager")
    public void create_DtoWithoutPriceByManager_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        requestDto.setPrice(null);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        
        assertTrue(Objects.requireNonNull(
                        result.getResolvedException()).getMessage()
                .contains("price can't be null, should be set"));
    }

    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Test
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new book with negative price by manager")
    public void create_DtoWithNegativePriceByManager_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        requestDto.setPrice(BigDecimal.valueOf(NEGATIVE_PRICE));
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        
        assertTrue(Objects.requireNonNull(
                        result.getResolvedException()).getMessage()
                .contains("price can't be less than 0"));
    }

    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Test
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new book without ISBN by manager")
    public void create_DtoWithoutIsbnByManager_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        requestDto.setIsbn(null);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        
        assertTrue(Objects.requireNonNull(
                        result.getResolvedException()).getMessage()
                .contains("ISBN can't be null, should be set"));
    }

    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Test
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new book with short ISBN by manager")
    public void create_DtoWithOneSymbolIsbnByManager_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        requestDto.setIsbn(SHORT_ISBN);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        
        assertTrue(Objects.requireNonNull(
                        result.getResolvedException()).getMessage()
                .contains("ISBN should be between 10 and 17 characters"));
    }

    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Test
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new book with characters in ISBN by manager")
    public void create_DtoWithIncorrectIsbnByManager_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        requestDto.setIsbn(INVALID_ISBN);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        
        assertTrue(Objects.requireNonNull(
                        result.getResolvedException()).getMessage()
                .contains("Invalid ISBN format"));
    }

    @WithMockUser(username = TEST_USER_CREDENTIALS, roles = {})
    @Test
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Try to create a new book with all valid fields by unauthenticated user")
    public void create_ValidRequestDtoWithAllFieldsByUnauthenticatedUser_ExceptionThrown()
            throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        
        String actual = Objects.requireNonNull(result.getResolvedException()).getMessage();
        assertEquals(ACCESS_DENIED_MESSAGE, actual);
    }

    @Test
    @WithMockUser(username = TEST_USER_CREDENTIALS)
    @DisplayName("Get book by correct id")
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getById_GetBookByCorrectIdByAuthenticatedUser_ReturnsBookDto() throws Exception {
        BookDto expected = bookDtoConstructor(
                CORRECT_ID_ONE,
                "The Book",
                "Modest Author",
                "978-3-16-148410-0",
                BigDecimal.valueOf(19.95),
                "Annotation",
                "scary url",
                new HashSet<>(Set.of(CORRECT_ID_ONE)));
        
        MvcResult result = mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andReturn();
        
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = TEST_USER_CREDENTIALS)
    @DisplayName("Try to get book by incorrect id")
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getById_GetBookByIncorrectIdByAuthenticatedUser_ExceptionThrown()
            throws Exception {
        String expected = "Can't find book by id: 42";
        
        MvcResult result = mockMvc.perform(get("/books/42"))
                .andExpect(status().isNotFound())
                .andReturn();
        
        String actual = Objects.requireNonNull(result.getResolvedException()).getMessage();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Try to get book by correct id by unauthenticated user")
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getById_GetBookByCorrectIdByUnauthenticatedUser_ExceptionThrown()
            throws Exception {
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @DisplayName("Delete book by correct id by manager")
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteById_DeleteBookByCorrectIdByManager_Success() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser(username = TEST_USER_CREDENTIALS)
    @DisplayName("Delete book by correct id by user")
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteById_DeleteBookByCorrectIdByUser_ExceptionThrown() throws Exception {

        MvcResult result = mockMvc.perform(delete("/books/1"))
                .andExpect(status().isForbidden())
                .andReturn();
        
        String actual = Objects.requireNonNull(result.getResolvedException()).getMessage();
        assertEquals(ACCESS_DENIED_MESSAGE, actual);
    }

    @Test
    @DisplayName("Delete book by correct id by unauthenticated user")
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteById_DeleteBookByCorrectIdByUnAuthenticatedUser_ExceptionThrown()
            throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @DisplayName("Delete book by incorrect id by manager")
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteById_DeleteBookByInCorrectIdByManager_ExceptionThrown() throws Exception {
        String expected = "Can't find book by id: 42";
        
        MvcResult result = mockMvc.perform(delete("/books/42"))
                .andExpect(status().isNotFound())
                .andReturn();
        
        String actual = Objects.requireNonNull(result.getResolvedException()).getMessage();
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = TEST_USER_CREDENTIALS)
    @DisplayName("Get all books by user")
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            "classpath:database/books/add-three-books-with-first-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAll_GetAllBooksByUser_ReturnsListOfBooks() throws Exception {
        BookDto firstBook = bookDtoConstructor(
                CORRECT_ID_ONE,
                "The First Book",
                "Old Doctor",
                "978-3-16-148410-0",
                BigDecimal.valueOf(29.99),
                "Something about medicine",
                "red url",
                new HashSet<>(Set.of(CORRECT_ID_ONE))
        );
        BookDto secondBook = bookDtoConstructor(
                CORRECT_ID_TWO,
                "The Second Book",
                "Scientist",
                "978-3-16-148410-1",
                BigDecimal.valueOf(9.50),
                "Something about science",
                "microscopic url",
                new HashSet<>(Set.of(CORRECT_ID_ONE))
        );
        BookDto thirdBook = bookDtoConstructor(
                CORRECT_ID_THREE,
                "The Third Book",
                "Engineer",
                "978-3-16-148410-3",
                BigDecimal.valueOf(19.00),
                "Something about cars",
                "speedy url",
                new HashSet<>(Set.of(CORRECT_ID_ONE))
        );
        List<BookDto> expected = new ArrayList<>();
        expected.add(firstBook);
        expected.add(secondBook);
        expected.add(thirdBook);
        String jsonRequest = objectMapper.writeValueAsString(STANDART_PAGEABLE);
        
        MvcResult result = mockMvc.perform(get("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        
        List<BookDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>(){});
        assertEquals(expected.size(), actual.size());
        EqualsBuilder.reflectionEquals(expected.get(0), actual.get(0));
        EqualsBuilder.reflectionEquals(expected.get(1), actual.get(1));
        EqualsBuilder.reflectionEquals(expected.get(2), actual.get(2));
    }

    @Test
    @DisplayName("Get all books by unauthenticated user")
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            "classpath:database/books/add-three-books-with-first-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAll_GetAllBooksByUnauthenticatedUser_ExceptionThrown() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(STANDART_PAGEABLE);
        
        mockMvc.perform(get("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Update a book with all valid fields by manager")
    public void update_UpdateAllFieldsByManager_ReturnsCorrectDto() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        BookDto expected = mapCreateDtoToDto(requestDto);
        expected.setId(CORRECT_ID_ONE);
        
        MvcResult result = mockMvc.perform(put("/books/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = TEST_USER_CREDENTIALS)
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Update a book with all valid fields by user")
    public void update_UpdateAllFieldsByUser_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/books/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        
        String actual = Objects.requireNonNull(result.getResolvedException()).getMessage();
        assertEquals(ACCESS_DENIED_MESSAGE, actual);
    }

    @Test
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Update a book with all valid fields by unauthenticated user")
    public void update_UpdateAllFieldsByUnAuthenticatedUser_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        
        mockMvc.perform(put("/books/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Try to update a book without title by manager")
    public void update_UpdateWithoutTitleByManager_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        requestDto.setTitle(null);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        
        MvcResult result = mockMvc.perform(put("/books/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        
        assertTrue(Objects.requireNonNull(
                        result.getResolvedException()).getMessage()
                .contains("title can't be null, should be set"));
    }

    @Test
    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Try to update a book without author by manager")
    public void update_UpdateWithoutAuthorByManager_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        requestDto.setAuthor(null);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        
        MvcResult result = mockMvc.perform(put("/books/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        
        assertTrue(Objects.requireNonNull(
                        result.getResolvedException()).getMessage()
                .contains("author can't be null, should be set"));
    }

    @Test
    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Try to update a book without isbn by manager")
    public void update_UpdateWithoutIsbnByManager_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        requestDto.setIsbn(null);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        
        MvcResult result = mockMvc.perform(put("/books/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertTrue(Objects.requireNonNull(
                        result.getResolvedException()).getMessage()
                .contains("ISBN can't be null, should be set"));
    }

    @Test
    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Try to update a book without price by manager")
    public void update_UpdateWithoutPriceByManager_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        requestDto.setPrice(null);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        
        MvcResult result = mockMvc.perform(put("/books/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        
        assertTrue(Objects.requireNonNull(
                        result.getResolvedException()).getMessage()
                .contains("price can't be null, should be set"));
    }

    @Test
    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Try to update a book with negative price by manager")
    public void update_UpdateWithNegativePriceByManager_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        requestDto.setPrice(BigDecimal.valueOf(NEGATIVE_PRICE));
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        
        MvcResult result = mockMvc.perform(put("/books/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        
        assertTrue(Objects.requireNonNull(
                        result.getResolvedException()).getMessage()
                .contains("price can't be less than 0"));
    }

    @Test
    @WithMockUser(username = TEST_MANAGER_CREDENTIALS, roles = {TEST_MANAGER_ROLE})
    @Sql(scripts = {REMOVE_ALL_BOOKS_AND_CATEGORIES,
            ADD_ONE_BOOK_WITH_CATEGORY},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Try to update a book with incorrect id by manager")
    public void update_UpdateWithIncorrectIdByManager_ExceptionThrown() throws Exception {
        CreateBookRequestDto requestDto = createBookRequest();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        String expected = "Can't find book by id: 42";
        
        MvcResult result = mockMvc.perform(put("/books/42")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        
        String actual = Objects.requireNonNull(result.getResolvedException()).getMessage();
        assertEquals(expected, actual);
    }

    private CreateBookRequestDto createBookRequest() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("The Mysterious Book");
        requestDto.setAuthor("Magic Author");
        requestDto.setIsbn("978-1-23-456789-0");
        requestDto.setPrice(BigDecimal.valueOf(99.99));
        requestDto.setDescription("Very first one");
        requestDto.setCoverImage("working url");
        requestDto.setCategoryIds(new HashSet<>(Set.of(CORRECT_ID_ONE)));
        return requestDto;
    }

    private BookDto bookDtoConstructor(
            Long id,
            String title,
            String author,
            String isbn,
            BigDecimal price,
            String description,
            String coverImage,
            Set<Long> categoryIds
    ) {
        BookDto bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setTitle(title);
        bookDto.setAuthor(author);
        bookDto.setIsbn(isbn);
        bookDto.setPrice(price);
        bookDto.setDescription(description);
        bookDto.setCoverImage(coverImage);
        bookDto.setCategoryIds(categoryIds);
        return bookDto;
    }

    private BookDto mapCreateDtoToDto(CreateBookRequestDto requestDto) {
        BookDto responseDto = new BookDto();
        responseDto.setId(CORRECT_ID_ONE);
        if (requestDto.getTitle() != null) {
            responseDto.setTitle(requestDto.getTitle());
        }
        if (requestDto.getAuthor() != null) {
            responseDto.setAuthor(requestDto.getAuthor());
        }
        if (requestDto.getIsbn() != null) {
            responseDto.setIsbn(requestDto.getIsbn());
        }
        if (requestDto.getPrice() != null) {
            responseDto.setPrice(requestDto.getPrice());
        }
        if (requestDto.getDescription() != null) {
            responseDto.setDescription(requestDto.getDescription());
        }
        if (requestDto.getCoverImage() != null) {
            responseDto.setCoverImage(requestDto.getCoverImage());
        }
        if (requestDto.getCategoryIds() != null) {
            responseDto.setCategoryIds(requestDto.getCategoryIds());
        }
        return responseDto;
    }
}
