package com.github.awrb.karaf.graphql.example.core;

import com.github.awrb.karaf.graphql.example.core.schema.Book;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collection;

@Component(service = GraphQLSchemaProvider.class)
public class GraphQLSchemaProvider {

    @Reference(service = BookRepository.class)
    private BookRepository bookRepository;

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public GraphQLSchema createSchema() {
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(
                this.getClass().getResourceAsStream("/schema.graphql"));

        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type("Mutation", builder -> builder.dataFetcher("addBook", addBookFetcher()))
                .type("Query", builder -> builder.dataFetcher("bookById", bookByIdFetcher()))
                .type("Query", builder -> builder.dataFetcher("books", booksFetcher()))
                .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
    }

    private DataFetcher<Book> addBookFetcher() {
        return environment -> {
            String name = environment.getArgument("name");
            int pageCount = environment.getArgument("pageCount");
            return bookRepository.createBook(name, pageCount);
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
}
