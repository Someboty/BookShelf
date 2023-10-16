package com.bookshop.repository;

import com.bookshop.model.ShoppingCart;
import com.bookshop.repository.cart.CartRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartRepositoryTests {
    private static final long ID_ONE = 1L;
    private static final long INCORRECT_ID = 100L;

    @Autowired
    private CartRepository cartRepository;

    @Test
    @DisplayName("Find shopping cart with correct user id")
    @Sql(scripts = {"classpath:database/carts/remove-all-users-carts-items.sql",
            "classpath:database/carts/add-one-user-with-empty-cart.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/carts/remove-all-users-carts-items.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getShoppingCartByUser_Id_CorrectUserId_ReturnsCart() {
        ShoppingCart expectedCart = new ShoppingCart();
        expectedCart.setId(ID_ONE);
        Optional<ShoppingCart> expected = Optional.of(expectedCart);

        Optional<ShoppingCart> actual = cartRepository.getShoppingCartByUser_Id(ID_ONE);

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Try to find shopping cart with incorrect user id")
    @Sql(scripts = {"classpath:database/carts/remove-all-users-carts-items.sql",
            "classpath:database/carts/add-one-user-with-empty-cart.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/carts/remove-all-users-carts-items.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getShoppingCartByUser_Id_IncorrectUserId_ReturnsEmpty() {
        Optional<ShoppingCart> expected = Optional.empty();

        Optional<ShoppingCart> actual = cartRepository.getShoppingCartByUser_Id(INCORRECT_ID);

        Assertions.assertTrue(actual.isEmpty());
        Assertions.assertEquals(expected, actual);
    }
}
