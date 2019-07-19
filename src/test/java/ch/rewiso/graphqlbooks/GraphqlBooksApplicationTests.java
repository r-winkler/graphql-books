package ch.rewiso.graphqlbooks;

import ch.rewiso.graphqlbooks.domain.Author;
import ch.rewiso.graphqlbooks.domain.Book;
import ch.rewiso.graphqlbooks.repository.AuthorRepository;
import ch.rewiso.graphqlbooks.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Do not use @GraphQLTest as we did not use their GraphQL java dependency and so we have to do use our autoconfiguration by using @ImportAutoConfiguration
// https://github.com/graphql-java-kickstart/graphql-spring-boot/blob/master/graphql-spring-boot-test-autoconfigure/src/main/java/com/graphql/spring/boot/test/GraphQLTest.java
public class GraphqlBooksApplicationTests {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;

    // Test data
    private Author author = new Author("Herbert", "Schildt");
    private Book book = new Book("Java: A Beginner's Guide, Sixth Edition", "0071809252", 728, author);


    @Test
    public void findAllBooks() throws IOException {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book));
        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/find-all-books.graphql");
        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();
        assertThat(response.get("$.data.findAllBooks[0].title")).isEqualTo("Java: A Beginner's Guide, Sixth Edition");
        assertThat(response.get("$.data.findAllBooks[0].isbn")).isEqualTo("0071809252");
    }

    @Test
    public void findBookByIsbn() throws IOException {
        when(bookRepository.findByIsbn("0071809252")).thenReturn(Optional.of(book));
        ObjectNode variables = new ObjectMapper().createObjectNode();
        variables.put("isbn", "0071809252");
        GraphQLResponse response = graphQLTestTemplate.perform("graphql/find-book-by-isbn.graphql", variables);
        assertThat(response).isNotNull();
        assertThat(response.isOk()).isTrue();
        assertThat(response.get("$.data.findBookByIsbn.title")).isEqualTo("Java: A Beginner's Guide, Sixth Edition");
        assertThat(response.get("$.data.findBookByIsbn.isbn")).isEqualTo("0071809252");
    }


}
