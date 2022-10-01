package com.github.awrb.karaf.graphql.example.api;

import java.util.Collection;

public interface BookRepository {

    Book storeBook(Book book);

    Collection<Book> getBooks();

    Book getBookById(String id);
}
