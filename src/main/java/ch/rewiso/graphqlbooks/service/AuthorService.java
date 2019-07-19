package ch.rewiso.graphqlbooks.service;

import ch.rewiso.graphqlbooks.domain.Author;
import ch.rewiso.graphqlbooks.repository.AuthorRepository;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@GraphQLApi
@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @GraphQLQuery
    public @GraphQLNonNull List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    @GraphQLQuery
    public @GraphQLNonNull long countAuthors() {
        return authorRepository.count();
    }

    @GraphQLMutation
    public @GraphQLNonNull Author newAuthor(@GraphQLNonNull String firstName, @GraphQLNonNull String lastName) {
        Author newAuthor = new Author(firstName, lastName);
        return authorRepository.save(newAuthor);
    }

}
