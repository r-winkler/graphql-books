package ch.rewiso.graphqlbooks;

import ch.rewiso.graphqlbooks.domain.Author;
import ch.rewiso.graphqlbooks.domain.Book;
import ch.rewiso.graphqlbooks.repository.AuthorRepository;
import ch.rewiso.graphqlbooks.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GraphqlBooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlBooksApplication.class, args);
    }


    @Bean
    public CommandLineRunner demo(AuthorRepository authorRepository, BookRepository bookRepository) {
        return (args) -> {
            Author author = new Author("Herbert", "Schildt");
            authorRepository.save(author);

            bookRepository.save(new Book("Java: A Beginner's Guide, Sixth Edition", "0071809252", 728, author));
        };
    }

}
