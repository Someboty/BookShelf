package com.bookshop.mapper;

import com.bookshop.config.MapperConfig;
import com.bookshop.dto.cart.CartItemDto;
import com.bookshop.dto.cart.CartItemDtoResponse;
import com.bookshop.dto.cart.CreateCartItemDto;
import com.bookshop.dto.cart.PutCartItemDto;
import com.bookshop.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toResponse(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toEntity(CartItemDto cartItemDto);

    @Mapping(source = "book.id", target = "bookId")
    CartItemDtoResponse toCreateDtoResponse(CartItem cartItem);

    PutCartItemDto toPutDto(CreateCartItemDto createCartItemDto);
}
