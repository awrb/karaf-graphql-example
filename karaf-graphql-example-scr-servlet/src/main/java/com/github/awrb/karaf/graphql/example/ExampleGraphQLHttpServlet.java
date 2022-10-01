package com.github.awrb.karaf.graphql.example;

import com.github.awrb.karaf.graphql.example.core.GraphQLSchemaProvider;
import graphql.kickstart.servlet.GraphQLConfiguration;
import graphql.kickstart.servlet.GraphQLHttpServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;


@Component(service = Servlet.class, property = {"alias=/graphql", "servlet-name=GraphQL"})
public class ExampleGraphQLHttpServlet extends GraphQLHttpServlet {

    @Reference
    private GraphQLSchemaProvider schemaProvider;

    public void setSchemaProvider(GraphQLSchemaProvider schemaProvider) {
        this.schemaProvider = schemaProvider;
    }

    @Override
    protected GraphQLConfiguration getConfiguration() {
        return GraphQLConfiguration.with(schemaProvider.createSchema()).build();
    }
}
