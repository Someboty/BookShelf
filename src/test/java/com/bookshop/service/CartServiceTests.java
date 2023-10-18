package com.bookshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.bookshop.dto.cart.request.CreateCartItemDto;
import com.bookshop.dto.cart.request.PutCartItemDto;
import com.bookshop.dto.cart.response.CartDto;
import com.bookshop.dto.cart.response.CartItemDto;
import com.bookshop.dto.cart.response.CartItemDtoResponse;
import com.bookshop.exception.EntityNotFoundException;
import com.bookshop.mapper.BookMapper;
import com.bookshop.mapper.CartItemMapper;
import com.bookshop.mapper.CartMapper;
import com.bookshop.model.Book;
import com.bookshop.model.CartItem;
import com.bookshop.model.Category;
import com.bookshop.model.ShoppingCart;
import com.bookshop.model.User;
import com.bookshop.repository.cart.CartRepository;
import com.bookshop.repository.cart.item.CartItemRepository;
import com.bookshop.repository.user.UserRepository;
import com.bookshop.service.impl.CartServiceImpl;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CartServiceTests {
    private static final String CANT_FIND_USER_MESSAGE = "Can't find user by id: ";
    private static final String CANT_FIND_CART_ITEM_MESSAGE = "Can't find cart item by id: ";
    private static final Long ID_ONE = 1L;
    private static final Long ID_INVALID = 42L;
    private static final int ONCE = 1;
    private static final int TWICE = 2;
    private static final Set<Category> FIRST_CATEGORY_SET =
            new HashSet<>(Set.of(new Category(ID_ONE)));

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartMapper cartMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookService bookService;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    @DisplayName("Get a cart by existing user with created cart")
    public void getCartInfo_GetExistingCartFromExistingUserId_ReturnsCartDto() {
        Long userId = ID_ONE;
        ShoppingCart expectedCart = new ShoppingCart();
        expectedCart.setId(userId);
        CartItem expectedItem = createCartItem(expectedCart);
        expectedCart.setCartItems(new HashSet<>(Set.of(expectedItem)));
        User user = createUser();
        expectedCart.setUser(user);
        CartDto expected = mapCartDtoFromCart(expectedCart);

        when(cartRepository.getShoppingCartByUser_Id(userId)).thenReturn(Optional.of(expectedCart));
        when(cartMapper.toCartDto(expectedCart)).thenReturn(expected);
        
        CartDto actual = cartService.getCartInfo(userId);

        assertEquals(expected, actual);
        assertEquals(expected.getCartItems(), actual.getCartItems());
        verify(cartRepository, times(ONCE)).getShoppingCartByUser_Id(userId);
        verifyNoMoreInteractions(cartRepository);
        verify(cartMapper, times(ONCE)).toCartDto(expectedCart);
        verifyNoMoreInteractions(cartMapper);
    }

    @Test
    @DisplayName("Get a cart by existing user with not previously created cart")
    public void getCartInfo_GetNewCartFromExistingUserId_ReturnsCartDto() {
        Long userId = ID_ONE;
        ShoppingCart expectedCart = new ShoppingCart();
        expectedCart.setId(userId);
        User user = createUser();
        expectedCart.setUser(user);
        CartDto expected = mapCartDtoFromCart(expectedCart);

        when(cartRepository.getShoppingCartByUser_Id(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cartRepository.save(expectedCart)).thenReturn(expectedCart);
        when(cartMapper.toCartDto(expectedCart)).thenReturn(expected);
        
        CartDto actual = cartService.getCartInfo(userId);
        
        assertEquals(expected, actual);
        assertEquals(expected.getCartItems(), actual.getCartItems());
        verify(cartRepository, times(ONCE)).getShoppingCartByUser_Id(userId);
        verify(cartRepository, times(ONCE)).save(expectedCart);
        verifyNoMoreInteractions(cartRepository);
        verify(cartMapper, times(ONCE)).toCartDto(expectedCart);
        verifyNoMoreInteractions(cartMapper);
    }

    @Test
    @DisplayName("Try to get a cart by non existing user")
    public void getCartInfo_GetCartFromNonExistingUserId_ExceptionThrown() {
        Long userId = ID_INVALID;
        String expected = CANT_FIND_USER_MESSAGE + ID_INVALID;
        when(cartRepository.getShoppingCartByUser_Id(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        Throwable exception = assertThrows(EntityNotFoundException.class,
                () -> cartService.getCartInfo(ID_INVALID));

        String actual = exception.getMessage();
        assertEquals(expected, actual);
        verify(cartRepository, times(ONCE)).getShoppingCartByUser_Id(userId);
        verify(userRepository, times(ONCE)).findById(userId);
        verifyNoMoreInteractions(cartRepository);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("Create cart item with valid cart, user and new book")
    public void createCartItem_CorrectData_ReturnsCartItemDtoResponse() {
        Long userId = ID_ONE;
        ShoppingCart expectedCart = new ShoppingCart();
        expectedCart.setId(userId);
        User user = createUser();
        expectedCart.setUser(user);
        CartItem expectedItem = createCartItem(expectedCart);
        expectedItem.setId(null);
        Book book = createValidBook();
        CartItemDtoResponse expected = mapCartItemToResponse(expectedItem);
        CreateCartItemDto request = createCartItemDto();

        when(cartRepository.getShoppingCartByUser_Id(userId)).thenReturn(Optional.of(expectedCart));
        doNothing().when(bookService).checkBookById(book.getId());
        when(bookMapper.bookFromId(request.getBookId())).thenReturn(book);
        when(cartItemRepository.save(expectedItem)).thenReturn(expectedItem);
        when(cartItemMapper.toCreateDtoResponse(expectedItem)).thenReturn(expected);

        CartItemDtoResponse actual = cartService.createCartItem(userId, request);

        assertEquals(expected, actual);
        verify(cartRepository, times(ONCE)).getShoppingCartByUser_Id(userId);
        verifyNoMoreInteractions(cartRepository);
        verify(cartItemRepository, times(ONCE)).save(expectedItem);
        verifyNoMoreInteractions(cartItemRepository);
        verify(bookMapper, times(ONCE)).bookFromId(request.getBookId());
        verifyNoMoreInteractions(bookMapper);
        verify(cartItemMapper, times(ONCE)).toCreateDtoResponse(expectedItem);
        verifyNoMoreInteractions(cartItemMapper);
    }

    @Test
    @DisplayName("Try to create cart item with valid cart, user and existing book")
    public void createCartItem_ExistingBook_ReturnsCartItemDtoResponse() {
        Long userId = ID_ONE;
        ShoppingCart expectedCart = new ShoppingCart();
        expectedCart.setId(userId);
        User user = createUser();
        expectedCart.setUser(user);
        CreateCartItemDto request = createCartItemDto();
        CartItem existingItem = createCartItem(expectedCart);
        existingItem.setQuantity(5);
        expectedCart.setCartItems(new HashSet<>(Set.of(existingItem)));
        CartItem expectedItem = createCartItem(expectedCart);
        expectedItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
        CartItemDtoResponse expected = mapCartItemToResponse(expectedItem);

        when(cartRepository.getShoppingCartByUser_Id(userId)).thenReturn(Optional.of(expectedCart));
        when(cartItemRepository.save(expectedItem)).thenReturn(expectedItem);
        when(cartItemMapper.toCreateDtoResponse(expectedItem)).thenReturn(expected);

        CartItemDtoResponse actual = cartService.createCartItem(userId, request);

        assertEquals(expected, actual);
        verify(cartRepository, times(TWICE)).getShoppingCartByUser_Id(userId);
        verifyNoMoreInteractions(cartRepository);
        verify(cartItemRepository, times(ONCE)).save(expectedItem);
        verifyNoMoreInteractions(cartItemRepository);
        verifyNoInteractions(bookMapper);
        verify(cartItemMapper, times(ONCE)).toCreateDtoResponse(expectedItem);
        verifyNoMoreInteractions(cartItemMapper);
    }

    @Test
    @DisplayName("Update cart item with valid cart, user and existing book")
    public void updateCartItem_ValidData_ReturnsCartItemDtoResponse() {
        Long userId = ID_ONE;
        ShoppingCart expectedCart = new ShoppingCart();
        expectedCart.setId(userId);
        User user = createUser();
        expectedCart.setUser(user);
        CartItem existingItem = createCartItem(expectedCart);
        expectedCart.setCartItems(new HashSet<>(Set.of(existingItem)));
        PutCartItemDto request = new PutCartItemDto();
        request.setQuantity(5);
        CartItem expectedItem = createCartItem(expectedCart);
        expectedItem.setQuantity(request.getQuantity());
        CartItemDtoResponse expected = mapCartItemToResponse(expectedItem);

        when(cartRepository.getShoppingCartByUser_Id(userId)).thenReturn(Optional.of(expectedCart));
        when(cartItemRepository.save(expectedItem)).thenReturn(expectedItem);
        when(cartItemMapper.toCreateDtoResponse(expectedItem)).thenReturn(expected);

        CartItemDtoResponse actual = cartService.updateCartItem(
                userId, existingItem.getId(), request);

        assertEquals(expected, actual);
        verify(cartRepository, times(ONCE)).getShoppingCartByUser_Id(userId);
        verifyNoMoreInteractions(cartRepository);
        verify(cartItemRepository, times(ONCE)).save(expectedItem);
        verifyNoMoreInteractions(cartItemRepository);
        verifyNoInteractions(bookMapper);
        verify(cartItemMapper, times(ONCE)).toCreateDtoResponse(expectedItem);
        verifyNoMoreInteractions(cartItemMapper);
    }

    @Test
    @DisplayName("Delete cart item with correct id")
    public void removeCartItem_CorrectId_Success() {
        Long userId = ID_ONE;
        ShoppingCart expectedCart = new ShoppingCart();
        expectedCart.setId(userId);
        CartItem expectedItem = createCartItem(expectedCart);
        expectedCart.setCartItems(new HashSet<>(Set.of(expectedItem)));
        User user = createUser();
        expectedCart.setUser(user);

        when(cartRepository.getShoppingCartByUser_Id(userId)).thenReturn(Optional.of(expectedCart));
        doNothing().when(cartItemRepository).delete(expectedItem);
        
        cartService.removeCartItem(userId, ID_ONE);
        
        verify(cartRepository, times(ONCE)).getShoppingCartByUser_Id(userId);
        verifyNoMoreInteractions(cartRepository);
        verify(cartItemRepository, times(ONCE)).delete(expectedItem);
        verifyNoMoreInteractions(cartItemRepository);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("Try to delete item with incorrect item id")
    public void removeCartItem_IncorrectItemId_ExceptionThrown() {
        Long userId = ID_ONE;
        ShoppingCart expectedCart = new ShoppingCart();
        expectedCart.setId(userId);
        CartItem expectedItem = createCartItem(expectedCart);
        expectedCart.setCartItems(new HashSet<>(Set.of(expectedItem)));
        User user = createUser();
        expectedCart.setUser(user);
        String expected = CANT_FIND_CART_ITEM_MESSAGE + ID_INVALID;

        when(cartRepository.getShoppingCartByUser_Id(userId)).thenReturn(Optional.of(expectedCart));
        
        Throwable exception = assertThrows(EntityNotFoundException.class,
                () -> cartService.removeCartItem(userId, ID_INVALID));
        
        String actual = exception.getMessage();
        assertEquals(expected, actual);
        verify(cartRepository, times(ONCE)).getShoppingCartByUser_Id(userId);
        verifyNoMoreInteractions(cartRepository);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("Try to delete item with incorrect user id")
    public void removeCartItem_IncorrectUserId_ExceptionThrown() {
        Long userId = ID_INVALID;
        String expected = CANT_FIND_USER_MESSAGE + ID_INVALID;

        when(cartRepository.getShoppingCartByUser_Id(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        Throwable exception = assertThrows(EntityNotFoundException.class,
                () -> cartService.removeCartItem(userId, ID_ONE));
        
        String actual = exception.getMessage();
        assertEquals(expected, actual);
        verify(cartRepository, times(ONCE)).getShoppingCartByUser_Id(userId);
        verifyNoMoreInteractions(cartRepository);
        verify(userRepository, times(ONCE)).findById(userId);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("Clear cart by correct user id")
    public void clearCart_CorrectUserId_Success() {
        Long userId = ID_ONE;
        ShoppingCart expectedCart = new ShoppingCart();
        expectedCart.setId(userId);
        User user = createUser();
        expectedCart.setUser(user);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(cartRepository.getShoppingCartByUser_Id(userId)).thenReturn(Optional.of(expectedCart));
        doNothing().when(cartItemRepository).deleteAllByShoppingCart(expectedCart);

        cartService.clearCart(userId);

        verify(cartRepository, times(ONCE)).getShoppingCartByUser_Id(userId);
        verifyNoMoreInteractions(cartRepository);
    }

    @Test
    @DisplayName("Try to clear cart by incorrect user id")
    public void clearCart_IncorrectUserId_ExceptionThrown() {
        Long userId = ID_INVALID;
        String expected = CANT_FIND_USER_MESSAGE + ID_INVALID;

        when(userRepository.existsById(userId)).thenReturn(false);

        Throwable exception = assertThrows(EntityNotFoundException.class,
                () -> cartService.clearCart(userId));

        String actual = exception.getMessage();
        assertEquals(expected, actual);
        verify(userRepository, times(ONCE)).existsById(userId);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(cartItemRepository);
    }

    private Book createValidBook() {
        Book book = new Book(ID_ONE);
        book.setTitle("Mysterious Title");
        book.setAuthor("Ambitious Author");
        book.setIsbn("978-1-23-456789-0");
        book.setPrice(BigDecimal.valueOf(9.85));
        book.setDescription("Detective book about murder in palace");
        book.setCoverImage("url of book cover image with blood on it");
        book.setCategories(FIRST_CATEGORY_SET);
        return book;
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }

    private CartItem createCartItem(ShoppingCart expectedCart) {
        CartItem item = new CartItem();
        item.setId(ID_ONE);
        item.setShoppingCart(expectedCart);
        item.setQuantity(3);
        item.setBook(createValidBook());
        return item;
    }

    private CreateCartItemDto createCartItemDto() {
        CreateCartItemDto dto = new CreateCartItemDto();
        dto.setBookId(ID_ONE);
        dto.setQuantity(3);
        return dto;
    }

    private CartItemDtoResponse mapCartItemToResponse(CartItem expectedItem) {
        CartItemDtoResponse response = new CartItemDtoResponse();
        response.setId(expectedItem.getId());
        response.setQuantity(expectedItem.getQuantity());
        response.setBookId(expectedItem.getBook().getId());
        return response;
    }

    private CartDto mapCartDtoFromCart(ShoppingCart expectedCart) {
        CartDto dto = new CartDto();
        dto.setId(expectedCart.getId());
        dto.setUserId(expectedCart.getUser().getId());
        dto.setCartItems(expectedCart.getCartItems().stream()
                .map(i -> {
                    CartItemDto itemDto = new CartItemDto();
                    itemDto.setId(i.getId());
                    itemDto.setQuantity(i.getQuantity());
                    itemDto.setBookId(i.getBook().getId());
                    itemDto.setBookTitle(i.getBook().getTitle());
                    return itemDto;
                })
                .collect(Collectors.toSet())
        );
        return dto;
    }
}
