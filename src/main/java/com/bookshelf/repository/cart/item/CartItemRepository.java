package com.bookshelf.repository.cart.item;

import com.bookshelf.model.CartItem;
import com.bookshelf.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByShoppingCart(ShoppingCart cart);
}
