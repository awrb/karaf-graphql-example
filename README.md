# karaf-graphql-example


http://localhost:8181/graphql?query={ bookById(id:"1") { name }}
http://localhost:8181/graphql?query={ bookById(id:"2") { name  id authorId pageCount}}

POST http://localhost:8181/graphql
{
  "query": "mutation { addBook(name:\"Test\", pageCount:100) { name } }"
}
