package code.shubham.graphql.web.v1.controllers.providers;

import code.shubham.graphql.web.v1.controllers.datafetchers.GraphQLDataFetchers;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@SuppressWarnings("Duplicates")
@Component
public class GraphQLProvider {

    @Autowired
    GraphQLDataFetchers graphQLDataFetchers;
    private GraphQL graphQL;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                    .dataFetcher("bookById", this.graphQLDataFetchers.getBookByIdDataFetcher())
                    .dataFetcher("books", this.graphQLDataFetchers.getBooksDataFetcher())
                    .dataFetcher("authors", this.graphQLDataFetchers.getAuthorsDataFetcher())
                    .dataFetcher("authorById", this.graphQLDataFetchers.getAuthorByIdDataFetcher())
                )
                .type(newTypeWiring("Mutation")
                      .dataFetcher("createAuthor", this.graphQLDataFetchers.createAuthor())
                      .dataFetcher("createBook", this.graphQLDataFetchers.createBook())
                      .dataFetcher("updateAuthorById", this.graphQLDataFetchers.updateAuthorById())
                      .dataFetcher("deleteAuthorById", this.graphQLDataFetchers.deleteAuthorById())
                )
                .type(newTypeWiring("Book")
                        .dataFetcher("author", this.graphQLDataFetchers.getAuthorDataFetcher())
                        .dataFetcher("pageCount", this.graphQLDataFetchers.getPageCountDataFetcher()))
                .build();
    }

}