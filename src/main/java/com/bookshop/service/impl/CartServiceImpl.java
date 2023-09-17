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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;

    @Override
    public CartDto getCartInfo(UserDetails userDetails) {
        ShoppingCart cart = findCart(userDetails);
        if (cart == null) {
            cart = createNewCart(userDetails);
        }
        return cartMapper.toCartDto(cart);
    }

    @Override
    public CartItemDtoResponse createCartItem(UserDetails userDetails, CreateCartItemDto request) {
        ShoppingCart cart = findCart(userDetails);
        CartItem item = new CartItem();
        item.setShoppingCart(cart);
        item.setBook(bookMapper.bookFromId(request.getBookId()));
        item.setQuantity(request.getQuantity());
        cart.getCartItems().add(item);
        return cartMapper.toItemResponse(item);
    }

    @Override
    public CartItemDtoResponse updateCartItem(UserDetails userDetails, int cartItemId, PutCartItemDto quantity) {
        return null;
    }

    private ShoppingCart findCart(UserDetails userDetails) {
        return cartRepository.getShoppingCartByUser_Email(
                userDetails.getUsername());
    }

    private ShoppingCart createNewCart(UserDetails userDetails) {
        ShoppingCart cart = new ShoppingCart();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NoSuchElementException("Can't find a user with email "
                        + email));
        cart.setId(user.getId());
        cart.setUser(user);
        return cart;
    }
}
