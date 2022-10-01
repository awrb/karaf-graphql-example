package com.github.awrb.karaf.graphql.example.core;

import com.github.awrb.karaf.graphql.example.api.BookRepository;
import com.github.awrb.karaf.graphql.example.api.Book;
import org.osgi.service.component.annotations.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component(service = BookRepository.class)
public class InMemoryBookRepository implements BookRepository {

    private final AtomicInteger idCounter = new AtomicInteger(0);
    private final Map<String, Book> booksById = new HashMap<>();

    public void activate() {
        createInitialBooks();
    }

    @Override
    public Book storeBook(Book book) {
        String id = nextId();
        book.setId(id);
        booksById.put(id, book);
        return book;
    }

    public Collection<Book> getBooks() {
        return booksById.values();
    }

    public Book getBookById(String id) {
        return booksById.get(id);
    }

    private void createInitialBooks() {
        storeBook(new Book("Apache Karaf Cookbook", 260));
        storeBook(new Book("Effective Java", 416));
        storeBook(new Book("OSGi in Action", 375));
    }

    private String nextId() {
        return Integer.toString(idCounter.incrementAndGet());
    }
}
