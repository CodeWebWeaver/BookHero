package com.parkhomovsky.bookstore.repository.book;

import com.parkhomovsky.bookstore.model.Book;
import com.parkhomovsky.bookstore.repository.SpecificationProvider;
import com.parkhomovsky.bookstore.repository.SpecificationProviderManager;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(b -> b.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new NoSuchElementException("Can`t find correct specification provider for key: "
                                + key));
    }
}
