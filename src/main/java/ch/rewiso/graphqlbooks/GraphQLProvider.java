package ch.rewiso.graphqlbooks;

import ch.rewiso.graphqlbooks.datafetcher.AuthorDataFetchers;
import ch.rewiso.graphqlbooks.datafetcher.BookDataFetchers;
import ch.rewiso.graphqlbooks.utils.CheckedFunction;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.stream.Stream;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {

    @Autowired
    private AuthorDataFetchers authorDataFetchers;

    @Autowired
    private BookDataFetchers bookDataFetchers;

    private GraphQL graphQL;

    @PostConstruct
    public void init() throws IOException {
        GraphQLSchema graphQLSchema = buildSchema("author.graphqls", "book.graphqls");
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String... graphqls) {
        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        Stream.of(graphqls).map(getSchemaDefinition()).forEach(sdl -> typeRegistry.merge(schemaParser.parse(sdl)));
        return schemaGenerator.makeExecutableSchema(typeRegistry, buildWiring());
    }

    private CheckedFunction<String, String> getSchemaDefinition() {
        return filename -> {
            URL url = Resources.getResource(filename);
            return Resources.toString(url, Charsets.UTF_8);
        };
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("findAllAuthors", authorDataFetchers.findAllAuthorsDataFetcher()))
                .type(newTypeWiring("Query")
                        .dataFetcher("countAuthors", authorDataFetchers.countAuthorsDataFetcher()))
                .type(newTypeWiring("Query")
                        .dataFetcher("findAllBooks", bookDataFetchers.findAllBooksDataFetcher()))
                .type(newTypeWiring("Query")
                        .dataFetcher("findBookByIsbn", bookDataFetchers.findBookByIsbnDataFetcher()))
                .type(newTypeWiring("Query")
                        .dataFetcher("countBooks", bookDataFetchers.countBooksDataFetcher()))
                .type(newTypeWiring("Book")
                        .dataFetcher("author", authorDataFetchers.findAuthorByBookDataFetcher()))
                .type(newTypeWiring("Mutation")
                        .dataFetcher("newAuthor", authorDataFetchers.newAuthorDataFetcher()))
                .type(newTypeWiring("Mutation")
                        .dataFetcher("newBook", bookDataFetchers.newBookDataFetcher()))
                .type(newTypeWiring("Mutation")
                        .dataFetcher("deleteBook", bookDataFetchers.deleteBookDataFetcher()))
                .type(newTypeWiring("Mutation")
                        .dataFetcher("updateBookPageCount", bookDataFetchers.updateBookPageCountDataFetcher()))
                .build();
    }

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

}
