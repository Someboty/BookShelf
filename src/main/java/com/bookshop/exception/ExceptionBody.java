package com.bookshop.exception;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatusCode;

public record ExceptionBody(
        LocalDateTime timestamp,
        HttpStatusCode status,
        List<String> errors) {
}
