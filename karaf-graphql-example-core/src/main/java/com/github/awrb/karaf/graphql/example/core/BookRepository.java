package com.github.awrb.karaf.graphql.example.core;

import com.github.awrb.karaf.graphql.example.core.schema.Book;
import org.osgi.service.component.annotations.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component(service = BookRepository.class)
public class BookRepository {

    private final AtomicInteger idCounter = new AtomicInteger(0);
    private final Map<String, Book> booksById = new HashMap<>();

    public void activate() {
        createInitialBooks();
    }

    public Book createBook(String name, int pageCount) {
        String id = nextId();
        Book book = new Book(id, name, pageCount);
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
        createBook("Apache Karaf Cookbook", 260);
        createBook("Effective Java", 416);
        createBook("OSGi in Action", 375);
    }

    private String nextId() {
        return Integer.toString(idCounter.incrementAndGet());
    }

}
