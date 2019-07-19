package ch.rewiso.graphqlbooks.repository;

import ch.rewiso.graphqlbooks.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
