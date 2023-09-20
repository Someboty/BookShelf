package com.bookshop.service.impl;

import com.bookshop.dto.cart.request.CreateCartItemDto;
import com.bookshop.dto.cart.request.PutCartItemDto;
import com.bookshop.dto.cart.response.CartDto;
import com.bookshop.dto.cart.response.CartItemDtoResponse;
import com.bookshop.mapper.BookMapper;
import com.bookshop.mapper.CartItemMapper;
import com.bookshop.mapper.CartMapper;
import com.bookshop.model.CartItem;
import com.bookshop.model.ShoppingCart;
import com.bookshop.model.User;
import com.bookshop.repository.cart.CartRepository;
import com.bookshop.repository.cart.item.CartItemRepository;
import com.bookshop.repository.user.UserRepository;
import com.bookshop.service.CartService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public CartDto getCartInfo(Long userId) {
        ShoppingCart cart = findCart(userId);
        if (cart == null) {
            User user = findUserById(userId);
            cart = new ShoppingCart();
            cart.setId(userId);
            cart.setUser(user);
            cartRepository.save(cart);
        }
        return cartMapper.toCartDto(cart);
    }

    @Override
    @Transactional
    public CartItemDtoResponse createCartItem(Long userId, CreateCartItemDto request) {
        ShoppingCart cart = findCart(userId);
        CartItem item = findCartItemByBookId(cart, request.getBookId());
        if (item != null) {
            return updateCartItem(userId, item.getId(), cartItemMapper.toPutDto(request));
        }
        item = new CartItem();
        item.setShoppingCart(cart);
        item.setBook(bookMapper.bookFromId(request.getBookId()));
        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        return cartItemMapper.toCreateDtoResponse(item);
    }

    @Override
    @Transactional
    public CartItemDtoResponse updateCartItem(
            Long userId,
            Long cartItemId,
            PutCartItemDto request) {
        ShoppingCart cart = findCart(userId);
        CartItem item = findCartItemById(cart, cartItemId);
        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        return cartItemMapper.toCreateDtoResponse(item);
    }

    @Override
    @Transactional
    public void removeCartItem(Long userId, Long cartItemId) {
        ShoppingCart cart = findCart(userId);
        findCartItemById(cart, cartItemId);
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        findCart(userId);
        cartItemRepository.deleteAllByShoppingCartId(findCart(userId).getId());
    }

    private ShoppingCart findCart(Long userId) {
        return cartRepository.getShoppingCartByUser_Id(userId);
    }

    private CartItem findCartItemById(ShoppingCart cart, Long cartItemId) {
        return cart.getCartItems().stream().filter(i -> i.getId()
                .equals(cartItemId))
                .findFirst()
                .orElseThrow(
                    () -> new NoSuchElementException("Can't find cart item with id " + cartItemId)
        );
    }

    private CartItem findCartItemByBookId(ShoppingCart cart, Long bookId) {
        return cart.getCartItems().stream()
                .filter(c -> c.getBook()
                        .getId()
                        .equals(bookId))
                .findFirst()
                .orElse(null);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("Can't find a user with id "
                        + userId));
    }
}
