package ch.rewiso.graphqlbooks.datafetcher;


import ch.rewiso.graphqlbooks.domain.Author;
import ch.rewiso.graphqlbooks.domain.Book;
import ch.rewiso.graphqlbooks.repository.AuthorRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AuthorDataFetchers {

    @Autowired
    private AuthorRepository authorRepository;

    public DataFetcher findAllAuthorsDataFetcher() {
        return dataFetchingEnvironment -> authorRepository.findAll();
    }

    public DataFetcher countAuthorsDataFetcher() {
        return dataFetchingEnvironment -> authorRepository.count();
    }

    public DataFetcher findAuthorByBookDataFetcher() {
        return dataFetchingEnvironment -> {
            Book book = dataFetchingEnvironment.getSource();
            return authorRepository.findById(book.getAuthor().getId()).orElse(null);
        };
    }

    public DataFetcher newAuthorDataFetcher() {
        return dataFetchingEnvironment -> {
            String firstName = dataFetchingEnvironment.getArgument("firstName");
            String lastName = dataFetchingEnvironment.getArgument("lastName");
            Author newAuthor = new Author(firstName, lastName);
            return authorRepository.save(newAuthor);
        };
    }

}
