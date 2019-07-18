package ch.rewiso.graphqlbooks.datafetcher;


import ch.rewiso.graphqlbooks.domain.Author;
import ch.rewiso.graphqlbooks.domain.Book;
import ch.rewiso.graphqlbooks.exception.BookNotFoundException;
import ch.rewiso.graphqlbooks.repository.AuthorRepository;
import ch.rewiso.graphqlbooks.repository.BookRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BookDataFetchers {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    public DataFetcher findAllBooksDataFetcher() {
        return dataFetchingEnvironment -> bookRepository.findAll();
    }

    public DataFetcher findBookByIsbnDataFetcher() {
        return dataFetchingEnvironment -> {
            String isbn = dataFetchingEnvironment.getArgument("isbn");
            return bookRepository.findByIsbn(isbn).orElse(null);
        };
    }

    public DataFetcher countBooksDataFetcher() {
        return dataFetchingEnvironment -> bookRepository.count();
    }

    public DataFetcher newBookDataFetcher() {
        return dataFetchingEnvironment -> {
            String title = dataFetchingEnvironment.getArgument("title");
            String isbn = dataFetchingEnvironment.getArgument("isbn");
            Integer pageCount = dataFetchingEnvironment.getArgument("pageCount");
            String authorId = dataFetchingEnvironment.getArgument("author");
            Author author = authorRepository.findById(Long.valueOf(authorId)).orElse(null);
            Book newBook = new Book(title, isbn, pageCount, author);
            return bookRepository.save(newBook);
        };
    }

    public DataFetcher deleteBookDataFetcher() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            bookRepository.deleteById(Long.valueOf(id));
            return id;
        };
    }

    public DataFetcher updateBookPageCountDataFetcher() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            Integer pageCount = dataFetchingEnvironment.getArgument("pageCount");
            Book updatedBook = bookRepository.findById(Long.valueOf(id))
                    .map(book -> {
                        book.setPageCount(pageCount);
                        return book;
                    }).orElseThrow(() -> new BookNotFoundException("The book to be updated was not found", Long.valueOf(id)));

            bookRepository.save(updatedBook);
            return updatedBook;
        };
    }


}
