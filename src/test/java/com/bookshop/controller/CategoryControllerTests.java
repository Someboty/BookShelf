package com.bookshop.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookshop.dto.book.response.BookDtoWithoutCategoryIds;
import com.bookshop.dto.category.request.CategoryDtoRequest;
import com.bookshop.dto.category.response.CategoryDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
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
public class CategoryControllerTests {
    protected static MockMvc mockMvc;
    private static final Pageable STANDART_PAGEABLE = PageRequest.of(0, 20);
    private static final Long CORRECT_ID_ONE = 1L;
    private static final Long CORRECT_ID_TWO = 2L;
    private static final Long CORRECT_ID_THREE = 3L;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"MANAGER"})
    @Test
    @Sql(scripts = "classpath:database/books/remove-all-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new category with all valid fields by manager")
    public void create_ValidCategoryDtoRequestWithAllFieldsByManager_ReturnsCorrectDto()
            throws Exception {
        //given
        CategoryDtoRequest requestDto = createCategoryDtoRequest();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        CategoryDto expected = mapCreateDtoToDto(requestDto);

        //when
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        //then
        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(scripts = "classpath:database/books/remove-all-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new category with all valid fields by user")
    public void create_ValidCategoryDtoRequestWithAllFieldsByUser_ExceptionThrown()
            throws Exception {
        //given
        CategoryDtoRequest requestDto = createCategoryDtoRequest();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //when
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        //then
        String expected = "Access Denied";
        String actual = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = "classpath:database/books/remove-all-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Try to create a new category with all valid fields by unauthenticated user")
    public void create_ValidCategoryDtoRequestWithAllFieldsByUnauthenticatedUser_ExceptionThrown()
            throws Exception {
        //given
        CategoryDtoRequest requestDto = createCategoryDtoRequest();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //when
        mockMvc.perform(post("/categories")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"MANAGER"})
    @Test
    @Sql(scripts = "classpath:database/books/remove-all-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new category without name by manager")
    public void create_DtoWithoutNameByManager_ExceptionThrown() throws Exception {
        //given
        CategoryDtoRequest requestDto = createCategoryDtoRequest();
        requestDto.setName(null);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //when
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        //then
        Assertions.assertTrue(Objects.requireNonNull(
                        result.getResolvedException()).getMessage()
                .contains("name can't be null"));
    }

    @WithMockUser(username = "admin", roles = {"MANAGER"})
    @Test
    @Sql(scripts = "classpath:database/books/remove-all-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new category without description by manager")
    public void create_DtoWithoutDescriptionByManager_ExceptionThrown() throws Exception {
        //given
        CategoryDtoRequest requestDto = createCategoryDtoRequest();
        requestDto.setDescription(null);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //when
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        //then
        Assertions.assertTrue(Objects.requireNonNull(
                        result.getResolvedException()).getMessage()
                .contains("description can't be null"));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Get category by correct id by authenticated user")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getById_GetCategoryByCorrectIdByAuthenticatedUser_ReturnsCategoryDto()
            throws Exception {
        //given
        CategoryDto expected = new CategoryDto();
        expected.setId(CORRECT_ID_ONE);
        expected.setName("drama");
        expected.setDescription("some sad stuff");

        //when
        MvcResult result = mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Try to get category by incorrect id")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getById_GetCategoryByIncorrectIdByAuthenticatedUser_ExceptionThrown()
            throws Exception {
        //given
        String expected = "Can't find category by id: 42";
        //when
        MvcResult result = mockMvc.perform(get("/categories/42"))
                .andExpect(status().isNotFound())
                .andReturn();

        //then
        String actual = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Try to get category by correct id by unauthenticated user")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getById_GetCategoryByCorrectIdByUnauthenticatedUser_ExceptionThrown()
            throws Exception {
        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"MANAGER"})
    @DisplayName("Delete category by correct id by manager")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteById_DeleteCategoryByCorrectIdByManager_Success() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Delete category by correct id by user")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteById_DeleteCategoryByCorrectIdByUser_ExceptionThrown() throws Exception {
        //given
        String expected = "Access Denied";

        //when
        MvcResult result = mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isForbidden())
                .andReturn();
        //then
        String actual = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Delete category by correct id by unauthenticated user")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteById_DeleteCategoryByCorrectIdByUnAuthenticatedUser_ExceptionThrown()
            throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"MANAGER"})
    @DisplayName("Delete category by incorrect id by manager")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteById_DeleteCategoryByInCorrectIdByManager_ExceptionThrown()
            throws Exception {
        //given
        String expected = "Can't find category by id: 42";
        //when
        MvcResult result = mockMvc.perform(delete("/categories/42"))
                .andExpect(status().isNotFound())
                .andReturn();
        //then
        String actual = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Get all categories by user")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-three-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAll_GetAllCategoriesByUser_ReturnsListOfCategories() throws Exception {
        //given
        CategoryDto firstCategory = categoryDtoConstructor(
                CORRECT_ID_ONE, "science", "some clever stuff");

        CategoryDto secondCategory = categoryDtoConstructor(
                CORRECT_ID_TWO, "biography", "about Person");

        CategoryDto thirdCategory = categoryDtoConstructor(
                CORRECT_ID_THREE, "comedy", "things that make you smile");

        List<CategoryDto> expected = new ArrayList<>();
        expected.add(firstCategory);
        expected.add(secondCategory);
        expected.add(thirdCategory);
        String jsonRequest = objectMapper.writeValueAsString(STANDART_PAGEABLE);

        //when
        MvcResult result = mockMvc.perform(get("/categories")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //then
        List<CategoryDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), new TypeReference<>(){}
        );
        Assertions.assertEquals(expected.size(), actual.size());
    }

    @Test
    @DisplayName("Get all categories by unauthenticated user")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-three-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getAll_GetAllCategoriesByUnauthenticatedUser_ExceptionThrown() throws Exception {
        //given
        String jsonRequest = objectMapper.writeValueAsString(STANDART_PAGEABLE);

        //when
        mockMvc.perform(get("/categories")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"MANAGER"})
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Update a category with all valid fields by manager")
    public void update_UpdateAllFieldsByManager_ReturnsCorrectDto() throws Exception {
        //given
        CategoryDtoRequest requestDto = createCategoryDtoRequest();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        CategoryDto expected = mapCreateDtoToDto(requestDto);
        expected.setId(CORRECT_ID_ONE);

        //when
        MvcResult result = mockMvc.perform(put("/categories/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //then
        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Update a category with all valid fields by user")
    public void update_UpdateAllFieldsByUser_ExceptionThrown() throws Exception {
        //given
        CategoryDtoRequest requestDto = createCategoryDtoRequest();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        String expected = "Access Denied";

        //when
        MvcResult result = mockMvc.perform(put("/categories/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        //then
        String actual = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Try to update a category with all valid fields by unauthenticated user")
    public void update_UpdateAllFieldsByUnAuthenticatedUser_ExceptionThrown() throws Exception {
        //given
        CategoryDtoRequest requestDto = createCategoryDtoRequest();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //when
        mockMvc.perform(put("/categories/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"MANAGER"})
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Try to update a category without name by manager")
    public void update_UpdateWithoutNameByManager_ExceptionThrown() throws Exception {
        //given
        CategoryDtoRequest requestDto = createCategoryDtoRequest();
        requestDto.setName(null);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //when
        MvcResult result = mockMvc.perform(put("/categories/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        //then
        Assertions.assertTrue(Objects.requireNonNull(
                        result.getResolvedException()).getMessage()
                .contains("name can't be null"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"MANAGER"})
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Try to update a category without description by manager")
    public void update_UpdateWithoutDescriptionByManager_ExceptionThrown() throws Exception {
        //given
        CategoryDtoRequest requestDto = createCategoryDtoRequest();
        requestDto.setDescription(null);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //when
        MvcResult result = mockMvc.perform(put("/categories/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        //then
        Assertions.assertTrue(Objects.requireNonNull(
                        result.getResolvedException()).getMessage()
                .contains("description can't be null"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"MANAGER"})
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Try to update a category with incorrect id by manager")
    public void update_UpdateWithIncorrectIdByManager_ExceptionThrown() throws Exception {
        //given
        CategoryDtoRequest requestDto = createCategoryDtoRequest();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        String expected = "Can't find category by id: 42";

        //when
        MvcResult result = mockMvc.perform(put("/categories/42")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        //then
        String actual = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Get all books by category id by user")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-ten-books-with-different-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getBooksByCategoryId_GetBooksByCorrectIdByUser_ReturnsListOfCategories()
            throws Exception {
        //given
        BookDtoWithoutCategoryIds firstBook = new BookDtoWithoutCategoryIds();
        firstBook.setId(CORRECT_ID_ONE);
        firstBook.setTitle("The First Book");
        firstBook.setAuthor("Old Doctor");
        firstBook.setIsbn("978-3-16-148410-0");
        firstBook.setPrice(BigDecimal.valueOf(29.99));
        firstBook.setDescription("Something about medicine");
        firstBook.setCoverImage("red url");

        BookDtoWithoutCategoryIds secondBook = new BookDtoWithoutCategoryIds();
        secondBook.setId(CORRECT_ID_ONE);
        secondBook.setTitle("The Second Book");
        secondBook.setAuthor("Scientist");
        secondBook.setIsbn("978-3-16-148410-1");
        secondBook.setPrice(BigDecimal.valueOf(9.50));
        secondBook.setDescription("Something about science");
        secondBook.setCoverImage("microscopic url");

        BookDtoWithoutCategoryIds thirdBook = new BookDtoWithoutCategoryIds();
        thirdBook.setId(CORRECT_ID_ONE);
        thirdBook.setTitle("The Third Book");
        thirdBook.setAuthor("Engineer");
        thirdBook.setIsbn("978-3-16-148410-2");
        thirdBook.setPrice(BigDecimal.valueOf(19.00));
        thirdBook.setDescription("Something about cars");
        thirdBook.setCoverImage("speedy url");
        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();
        expected.add(firstBook);
        expected.add(secondBook);
        expected.add(thirdBook);
        String jsonRequest = objectMapper.writeValueAsString(STANDART_PAGEABLE);

        //when
        MvcResult result = mockMvc.perform(get("/categories/1/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //then
        List<BookDtoWithoutCategoryIds> actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        new TypeReference<>(){});
        Assertions.assertEquals(expected.size(), actual.size());
        EqualsBuilder.reflectionEquals(expected.get(0), actual.get(0));
        EqualsBuilder.reflectionEquals(expected.get(1), actual.get(1));
        EqualsBuilder.reflectionEquals(expected.get(2), actual.get(2));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("Get all books by incorrect category id by user")
    @Sql(scripts = {"classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-ten-books-with-different-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getBooksByCategoryId_GetBooksByIncorrectCorrectIdByUser_ReturnsListOfCategories()
            throws Exception {
        //given
        String jsonRequest = objectMapper.writeValueAsString(STANDART_PAGEABLE);
        String expected = "Can't find category by id: 42";

        //when
        MvcResult result = mockMvc.perform(get("/categories/42")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        //then
        String actual = Objects.requireNonNull(result.getResolvedException()).getMessage();
        Assertions.assertEquals(expected, actual);
    }

    private CategoryDto mapCreateDtoToDto(CategoryDtoRequest requestDto) {
        CategoryDto result = new CategoryDto();
        result.setId(CORRECT_ID_ONE);
        result.setName(requestDto.getName());
        result.setDescription(requestDto.getDescription());
        return result;
    }

    private CategoryDtoRequest createCategoryDtoRequest() {
        CategoryDtoRequest request = new CategoryDtoRequest();
        request.setName("Detective");
        request.setDescription("Detective stories");
        return request;
    }

    private CategoryDto categoryDtoConstructor(
            Long id,
            String name,
            String description
    ) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(id);
        categoryDto.setName(name);
        categoryDto.setDescription(description);
        return categoryDto;
    }
}
