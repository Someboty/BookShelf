package com.bookshop.repository.cart;

import com.bookshop.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart getShoppingCartByUser_Email(String email);
}
