package com.github.awrb.karaf.graphql.example.core;

import com.github.awrb.karaf.graphql.example.api.Book;
import com.github.awrb.karaf.graphql.example.api.BookRepository;
import com.github.awrb.karaf.graphql.example.api.GraphQLSchemaProvider;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.reactivestreams.Publisher;

import java.util.Collection;


@Component(service = GraphQLSchemaProvider.class)
public class BookSchemaProvider implements GraphQLSchemaProvider {

    @Reference(service = BookRepository.class)
    private BookRepository bookRepository;

    private PublishSubject<Book> subject;

    public void setBookRepository(InMemoryBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Activate
    public void activate() {
        subject = PublishSubject.create();
    }

    @Override
    public GraphQLSchema createSchema() {
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(
                this.getClass().getResourceAsStream("/schema.graphql"));

        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type("Mutation", builder -> builder.dataFetcher("addBook", addBookFetcher()))
                .type("Query", builder -> builder.dataFetcher("bookById", bookByIdFetcher()))
                .type("Query", builder -> builder.dataFetcher("books", booksFetcher()))
                .type("Subscription", builder -> builder.dataFetcher("bookCreated", bookCreatedFetcher()))
                .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
    }

    private DataFetcher<Book> addBookFetcher() {
        return environment -> {
            String name = environment.getArgument("name");
            int pageCount = environment.getArgument("pageCount");
            Book book = bookRepository.storeBook(new Book(name, pageCount));
            subject.onNext(book);
            return book;
        };
    }

    private DataFetcher<Book> bookByIdFetcher() {
        return environment -> {
            String id = environment.getArgument("id");
            return bookRepository.getBookById(id);
        };
    }

    private DataFetcher<Collection<Book>> booksFetcher() {
        return environment -> bookRepository.getBooks();
    }

    private DataFetcher<Publisher<Book>> bookCreatedFetcher() {
        return environment -> getPublisher();
    }

    private Publisher<Book> getPublisher() {
        subject = PublishSubject.create();
        ConnectableObservable<Book> connectableObservable = subject.share().publish();
        connectableObservable.connect();
        return connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }
}
