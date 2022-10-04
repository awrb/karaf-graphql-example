package com.github.awrb.karaf.graphql.example.websocket;

import com.github.awrb.karaf.graphql.example.api.GraphQLSchemaProvider;
import graphql.GraphQL;
import graphql.execution.SubscriptionExecutionStrategy;
import org.eclipse.jetty.websocket.servlet.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "Example GraphQL WebSocket Servlet", urlPatterns = {"/graphql-websocket"})
@Component(service = Servlet.class, property = {"osgi.http.whiteboard.servlet.pattern=/graphql-websocket"})
public class GraphQLWebSocketServlet extends WebSocketServlet implements WebSocketCreator {

    @Reference(service = GraphQLSchemaProvider.class)
    private GraphQLSchemaProvider schemaProvider;

    @Override
    public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) {
        GraphQL graphQL = GraphQL.newGraphQL(schemaProvider.createSchema())
                .subscriptionExecutionStrategy(new SubscriptionExecutionStrategy())
                .build();
        return new GraphQLWebSocketExample(graphQL);
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.setCreator(this);
    }
}
