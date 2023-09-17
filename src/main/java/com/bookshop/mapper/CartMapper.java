package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.cart.CartDto;
import com.bookshop.dto.cart.CartItemDtoResponse;
import com.bookshop.model.CartItem;
import com.bookshop.model.ShoppingCart;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CartMapper {
    default CartDto toCartDto(ShoppingCart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getId());
        Set<CartItemDtoResponse> items = new HashSet<>();
        if (!cart.getCartItems().isEmpty()) {
            items = cart.getCartItems().stream()
                    .map(i -> {
                        CartItemDtoResponse response = new CartItemDtoResponse();
                        response.setId(i.getId());
                        response.setQuantity(i.getQuantity());
                        response.setBookId(i.getBook().getId());
                        response.setBookTitle(i.getBook().getTitle());
                        return response;
                    }).collect(Collectors.toSet());
        }
        return dto;
    }

    CartItemDtoResponse toItemResponse(CartItem cartItem);
}
