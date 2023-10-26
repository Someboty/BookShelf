package com.bookshelf.mapper;

import com.bookshelf.config.MapperConfig;
import com.bookshelf.dto.cart.request.CreateCartItemDto;
import com.bookshelf.dto.cart.request.PutCartItemDto;
import com.bookshelf.dto.cart.response.CartItemDto;
import com.bookshelf.dto.cart.response.CartItemDtoResponse;
import com.bookshelf.model.CartItem;
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
