package ch.rewiso.graphqlbooks.service;

import ch.rewiso.graphqlbooks.domain.Author;
import ch.rewiso.graphqlbooks.domain.Book;
import ch.rewiso.graphqlbooks.exception.BookNotFoundException;
import ch.rewiso.graphqlbooks.repository.AuthorRepository;
import ch.rewiso.graphqlbooks.repository.BookRepository;
import io.leangen.graphql.annotations.*;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@GraphQLApi
@Service
public class BookService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @GraphQLQuery
    public @GraphQLNonNull List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @GraphQLQuery
    public Book findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).orElse(null);
    }

    @GraphQLQuery
    public @GraphQLNonNull long countBooks() {
        return bookRepository.count();
    }

    @GraphQLQuery(name = "author")
    public Author findAuthorByBook(@GraphQLContext Book book) {
        return authorRepository.findById(book.getAuthor().getId()).orElse(null);
    }

    @GraphQLMutation
    public @GraphQLNonNull Book newBook(@GraphQLNonNull String title, @GraphQLNonNull String isbn, int pageCount, @GraphQLNonNull @GraphQLArgument(name = "author") long authorId) {
        Author author = authorRepository.findById(authorId).orElse(null);
        Book newBook = new Book(title, isbn, pageCount, author);
        return bookRepository.save(newBook);
    }

    @GraphQLMutation
    public @GraphQLNonNull Long deleteBook(@GraphQLNonNull Long id) {
        bookRepository.deleteById(id);
        return id;
    }

    @GraphQLMutation
    public @GraphQLNonNull Book updateBookPageCount(@GraphQLNonNull Long id, @GraphQLNonNull int pageCount) {
        Book updatedBook = bookRepository.findById(id)
                .map(book -> {
                    book.setPageCount(pageCount);
                    return book;
                }).orElseThrow(() -> new BookNotFoundException("The book to be updated was not found", id));

        bookRepository.save(updatedBook);
        return updatedBook;
    }


}




