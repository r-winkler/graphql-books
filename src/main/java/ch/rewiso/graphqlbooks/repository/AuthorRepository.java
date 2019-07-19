package ch.rewiso.graphqlbooks.repository;

import ch.rewiso.graphqlbooks.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
