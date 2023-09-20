package com.bookshop.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ShippingAddressRequestDto {
    @NotNull
    @Schema(example = "Ukraine, Kyiv, Maidan Nezalezhnosti")
    @Size(min = 10, max = 255,
            message = "shipping address should be between 10 and 255 characters")
    private String shippingAddress;
}
