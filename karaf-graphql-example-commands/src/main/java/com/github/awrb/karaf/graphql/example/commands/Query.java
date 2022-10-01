package com.github.awrb.karaf.graphql.example.commands;

import com.github.awrb.karaf.graphql.example.api.GraphQLSchemaProvider;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;

/**
 * Sample calls:
 * graphql:query "{books { name }}"
 * graphql:query "{bookById(id:\"1\") { name id pageCount }}"
 * graphql:query "mutation { addBook(name:\"Lord of the Rings\" pageCount:100) { id name }}
 */
@Service
@Command(scope = "graphql", name = "query", description = "Execute GraphQL query")
public class Query implements Action {

    @Argument(index = 0, name = "query", required = true, multiValued = false)
    String query;

    @Reference
    private GraphQLSchemaProvider schemaProvider;

    @Override
    public Object execute() throws Exception {
        GraphQLSchema schema = schemaProvider.createSchema();
        ExecutionResult result = GraphQL.newGraphQL(schema).build().execute(query);
        if (result.getData() != null) {
            System.out.println(result.getData().toString());
        } else {
            result.getErrors().forEach(System.out::println);
        }
        return null;
    }
}
