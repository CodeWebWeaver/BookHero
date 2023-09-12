package com.parkhomovsky.bookstore;

import com.parkhomovsky.bookstore.model.Book;
import com.parkhomovsky.bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookheroApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookheroApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book breakingBadBook = new Book();
                breakingBadBook.setAuthor("Greg");
                breakingBadBook.setIsbn("123543");
                breakingBadBook.setPrice(BigDecimal.valueOf(233));
                breakingBadBook.setTitle("Breaking Bad");

                Book myLittlePonyBook = new Book();
                myLittlePonyBook.setAuthor("Margaret");
                myLittlePonyBook.setIsbn("15626");
                myLittlePonyBook.setPrice(BigDecimal.valueOf(111));
                myLittlePonyBook.setTitle("My Little Pony");

                bookService.create(breakingBadBook);
                bookService.create(myLittlePonyBook);
                bookService.getAll();
            }
        };
    }
}
