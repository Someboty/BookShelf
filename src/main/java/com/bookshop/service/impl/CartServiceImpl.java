package com.bookshop.service.impl;

import com.bookshop.dto.cart.CartDto;
import com.bookshop.dto.cart.CartItemDtoResponse;
import com.bookshop.dto.cart.CreateCartItemDto;
import com.bookshop.dto.cart.PutCartItemDto;
import com.bookshop.mapper.BookMapper;
import com.bookshop.mapper.CartMapper;
import com.bookshop.model.CartItem;
import com.bookshop.model.ShoppingCart;
import com.bookshop.model.User;
import com.bookshop.repository.cart.CartRepository;
import com.bookshop.repository.user.UserRepository;
import com.bookshop.service.CartService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;

    @Override
    public CartDto getCartInfo(Long userId) {
        ShoppingCart cart = findCart(userId);
        if (cart == null) {
            cart = createNewCart(userId);
        }
        return cartMapper.toCartDto(cart);
    }

    @Override
    public CartItemDtoResponse createCartItem(Long userId, CreateCartItemDto request) {
        ShoppingCart cart = findCart(userId);
        CartItem item = new CartItem();
        item.setShoppingCart(cart);
        item.setBook(bookMapper.bookFromId(request.getBookId()));
        item.setQuantity(request.getQuantity());
        cart.getCartItems().add(item);
        return cartMapper.toItemResponse(item);
    }

    @Override
    public CartItemDtoResponse updateCartItem(
            Long userId,
            int cartItemId,
            PutCartItemDto quantity) {
        return null;
    }

    private ShoppingCart findCart(Long userId) {
        return cartRepository.getShoppingCartByUser_Id(userId);
    }

    private ShoppingCart createNewCart(Long userId) {
        ShoppingCart cart = new ShoppingCart();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("Can't find a user with id "
                        + userId));
        cart.setId(user.getId());
        cart.setUser(user);
        return cart;
    }
}
