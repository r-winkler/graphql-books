package ch.rewiso.graphqlbooks.exception;

public class BookNotFoundException extends RuntimeException {


    private Long invalidId;

    public BookNotFoundException(String message, Long invalidBookId) {
        super(message);
        this.invalidId = invalidBookId;
    }

    public Long getInvalidId() {
        return invalidId;
    }
}
