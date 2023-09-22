package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.cart.CartDto;
import com.bookshop.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface CartMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItems")
    CartDto toCartDto(ShoppingCart cart);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCart toEntity(CartDto cartDto);
}
