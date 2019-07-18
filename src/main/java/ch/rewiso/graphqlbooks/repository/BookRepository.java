package ch.rewiso.graphqlbooks.repository;

import ch.rewiso.graphqlbooks.domain.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

}
