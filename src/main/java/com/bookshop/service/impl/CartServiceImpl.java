package com.bookshop.service.impl;

import com.bookshop.dto.cart.request.CreateCartItemDto;
import com.bookshop.dto.cart.request.PutCartItemDto;
import com.bookshop.dto.cart.response.CartDto;
import com.bookshop.dto.cart.response.CartItemDtoResponse;
import com.bookshop.exception.EntityNotFoundException;
import com.bookshop.mapper.BookMapper;
import com.bookshop.mapper.CartItemMapper;
import com.bookshop.mapper.CartMapper;
import com.bookshop.model.CartItem;
import com.bookshop.model.ShoppingCart;
import com.bookshop.model.User;
import com.bookshop.repository.cart.CartRepository;
import com.bookshop.repository.cart.item.CartItemRepository;
import com.bookshop.repository.user.UserRepository;
import com.bookshop.service.BookService;
import com.bookshop.service.CartService;
import java.util.Optional;
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
    private final BookService bookService;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public CartDto getCartInfo(Long userId) {
        ShoppingCart cart = findCart(userId);
        return cartMapper.toCartDto(cart);
    }

    @Override
    @Transactional
    public CartItemDtoResponse createCartItem(Long userId, CreateCartItemDto request) {
        ShoppingCart cart = findCart(userId);
        Optional<CartItem> optionalCartItem = findCartItemByBookId(cart, request.getBookId());
        if (optionalCartItem.isPresent()) {
            PutCartItemDto putCartItemDto = new PutCartItemDto();
            putCartItemDto.setQuantity(
                    request.getQuantity() + optionalCartItem.get().getQuantity());
            return updateCartItem(userId,
                    optionalCartItem.get().getId(),
                    putCartItemDto);
        }
        checkIsBookValid(request.getBookId());
        CartItem item = new CartItem();
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
        CartItem item = findCartItemById(findCart(userId), cartItemId);
        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        return cartItemMapper.toCreateDtoResponse(item);
    }

    @Override
    @Transactional
    public void removeCartItem(Long userId, Long cartItemId) {
        cartItemRepository.delete(findCartItemById(findCart(userId), cartItemId));
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        checkUserById(userId);
        cartItemRepository.deleteAllByShoppingCart(findCart(userId));
    }

    private ShoppingCart findCart(Long userId) {
        Optional<ShoppingCart> cart = cartRepository.getShoppingCartByUser_Id(userId);
        return cart.orElseGet(() -> cart.orElse(createCart(userId)));
    }

    private CartItem findCartItemById(ShoppingCart cart, Long cartItemId) {
        return cart.getCartItems().stream().filter(i -> i.getId()
                .equals(cartItemId))
                .findFirst()
                .orElseThrow(
                    () -> new EntityNotFoundException("Can't find cart item by id: " + cartItemId)
        );
    }

    private Optional<CartItem> findCartItemByBookId(ShoppingCart cart, Long bookId) {
        return cart.getCartItems().stream()
                .filter(c -> c.getBook()
                        .getId()
                        .equals(bookId))
                .findFirst();
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by id: "
                        + userId));
    }

    private ShoppingCart createCart(Long userId) {
        User user = findUserById(userId);
        ShoppingCart cart = new ShoppingCart();
        cart.setId(userId);
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    private void checkIsBookValid(Long bookId) {
        bookService.checkBookById(bookId);
    }

    private void checkUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Can't find user by id: " + userId);
        }
    }
}
