package com.bookshelf.dto.order.request;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@EqualsAndHashCode
public class StatusRequestDto {
    @NotNull(message = "Status can't be null")
    @Length(min = 2, max = 12, message = "Status length should be between 2 and 12")
    private String status;
}
