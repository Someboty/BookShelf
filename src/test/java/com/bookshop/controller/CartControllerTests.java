package com.bookshop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bookshop.dto.cart.request.CreateCartItemDto;
import com.bookshop.dto.cart.request.PutCartItemDto;
import com.bookshop.dto.cart.response.CartDto;
import com.bookshop.dto.cart.response.CartItemDto;
import com.bookshop.dto.cart.response.CartItemDtoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CartControllerTests {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails(value = "test@mail.com")
    @Sql(scripts = {"classpath:database/carts/remove-all-users-carts-items.sql",
            "classpath:database/carts/add-one-user-with-cart-with-item.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/carts/remove-all-users-carts-items.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get cart by user")
    public void getCartInfo_CorrectData_ReturnsCorrectDto() throws Exception {
        CartDto expected = cartDtoFromDb();

        MvcResult result = mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andReturn();

        CartDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CartDto.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails(value = "test@mail.com")
    @Sql(scripts = {"classpath:database/carts/remove-all-users-carts-items.sql",
            "classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/carts/add-one-user-with-empty-cart.sql",
            "classpath:database/books/add-one-book-with-first-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/carts/remove-all-users-carts-items.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Create cart item by correct data")
    public void createCartItem_CorrectData_ReturnsCorrectDto() throws Exception {
        CreateCartItemDto request = createCartItemDto();
        String jsonRequest = objectMapper.writeValueAsString(request);
        CartItemDtoResponse expected = mapCartItemDtoResponseFromCreateCartItemDto(request);

        MvcResult result = mockMvc.perform(post("/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CartItemDtoResponse actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CartItemDtoResponse.class);
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithUserDetails(value = "test@mail.com")
    @Sql(scripts = {"classpath:database/carts/remove-all-users-carts-items.sql",
            "classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-book-with-first-category.sql",
            "classpath:database/carts/add-one-user-with-cart-with-item.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/carts/remove-all-users-carts-items.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update cart item by correct data")
    public void updateCartItem_CorrectData_ReturnsCorrectDto() throws Exception {
        PutCartItemDto request = putCartItemDto();
        String jsonRequest = objectMapper.writeValueAsString(request);
        CartItemDtoResponse expected =
                mapCartItemDtoResponseFromCreateCartItemDto(createCartItemDto());
        expected.setQuantity(request.getQuantity());
        expected.setId(1L);

        MvcResult result = mockMvc.perform(put("/cart/cart-items/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartItemDtoResponse actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CartItemDtoResponse.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithUserDetails(value = "test@mail.com")
    @Sql(scripts = {"classpath:database/carts/remove-all-users-carts-items.sql",
            "classpath:database/carts/add-one-user-with-cart-with-item.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/carts/remove-all-users-carts-items.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Delete cart item by correct data")
    public void deleteCartItem_CorrectData_Success() throws Exception {
        mockMvc.perform(delete("/cart/cart-items/1"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithUserDetails(value = "test@mail.com")
    @Sql(scripts = {"classpath:database/carts/remove-all-users-carts-items.sql",
            "classpath:database/books/remove-all-books-and-categories.sql",
            "classpath:database/books/add-one-book-with-first-category.sql",
            "classpath:database/carts/add-one-user-with-cart-with-item.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/carts/remove-all-users-carts-items.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Clear cart")
    public void clearCart_CorrectData_Success() throws Exception {
        mockMvc.perform(delete("/cart"))
                .andExpect(status().isOk())
                .andReturn();
    }

    private CartDto cartDtoFromDb() {
        CartDto cartDto = new CartDto();
        cartDto.setId(1L);
        cartDto.setUserId(1L);
        CartItemDto item = new CartItemDto();
        item.setId(1L);
        item.setBookId(1L);
        item.setQuantity(5);
        cartDto.setCartItems(new HashSet<>(Set.of(item)));
        return cartDto;
    }

    private CreateCartItemDto createCartItemDto() {
        CreateCartItemDto request = new CreateCartItemDto();
        request.setBookId(1L);
        request.setQuantity(5);
        return request;
    }

    private PutCartItemDto putCartItemDto() {
        PutCartItemDto request = new PutCartItemDto();
        request.setQuantity(10);
        return request;
    }

    private CartItemDtoResponse mapCartItemDtoResponseFromCreateCartItemDto(
            CreateCartItemDto request) {
        CartItemDtoResponse response = new CartItemDtoResponse();
        response.setBookId(request.getBookId());
        response.setQuantity(request.getQuantity());
        return response;
    }
}
