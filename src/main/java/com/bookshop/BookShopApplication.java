package com.bookshop;

import com.bookshop.model.Book;
import com.bookshop.service.BookService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class BookShopApplication {
    private final BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookShopApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book colourOfMagic = new Book();
            colourOfMagic.setAuthor("Sir Terence David John «Terry» Pratchett");
            colourOfMagic.setTitle("The Colour of Magic");
            colourOfMagic.setIsbn("0062225677");
            colourOfMagic.setPrice(BigDecimal.valueOf(4.99));

            bookService.save(colourOfMagic);

            System.out.println(bookService.findAll());
        };
    }
}
