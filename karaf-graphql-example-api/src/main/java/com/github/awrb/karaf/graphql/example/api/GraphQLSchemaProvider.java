package com.github.awrb.karaf.graphql.example.api;

import graphql.schema.GraphQLSchema;

public interface GraphQLSchemaProvider {

    GraphQLSchema createSchema();
}
