package com.bookshelf.repository.cart;

import com.bookshelf.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("FROM ShoppingCart sc LEFT JOIN FETCH sc.cartItems WHERE sc.user.id = :id")
    Optional<ShoppingCart> getShoppingCartByUser_Id(Long id);
}
