# karaf-graphql-example
This demonstrates GraphQL usage in Apache Karaf 4.4.x, either via a servlet or via Karaf commands.

Build with JDK 11 and Maven 3.x:

`mvn clean install`

Then start Apache Karaf, and install the feature:
```
feature:repo-add mvn:com.github.awrb/karaf-graphql-example-features/4.4.1/xml
feature:install karaf-graphql-example
```

A HTTP server will start on the configured port in Karaf (8181 by default). 
The following endpoints can be used to test the GraphQL API:
```
GET http://localhost:8181/graphql?query={ bookById(id:"1") { name }}
GET http://localhost:8181/graphql?query={ bookById(id:"2") { name  id authorId pageCount}}

POST http://localhost:8181/graphql
{
  "query": "mutation { addBook(name:\"Test\", pageCount:100) { name } }"
}
```

Additionally, a `graphql:query` command will be available. It takes a single mandatory argument
which needs to be a valid GraphQL query in the defined GraphQL schema.
For instance:
```
karaf@root()> graphql:query "{books { name id pageCount }}"
{books=[{name=Apache Karaf Cookbook, id=1, pageCount=260}, {name=Effective Java, id=2, pageCount=416}, {name=OSGi in Action, id=3, pageCount=375}]}

karaf@root()> graphql:query "{bookById(id:1) { name id pageCount }}"                                                                                                                  
{bookById={name=Apache Karaf Cookbook, id=1, pageCount=260}}

karaf@root()> graphql:query "mutation { addBook(name:\"Lord of the Rings\" pageCount:100) { id name }}"
{addBook={id=9, name=Lord of the Rings}}
```

TODOs:
- add a WebSocket servlet
- add examples with more complex schemas
- add examples for HTTP/WebSocket GraphQL clients (async or not)
- improve bundle/feature/POM names
- add itests