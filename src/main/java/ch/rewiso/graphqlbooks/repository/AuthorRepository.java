package ch.rewiso.graphqlbooks.repository;

import ch.rewiso.graphqlbooks.domain.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {

}
