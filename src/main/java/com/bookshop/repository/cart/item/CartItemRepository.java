package com.bookshop.repository.cart.item;

import com.bookshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByShoppingCartId(Long id);
}
