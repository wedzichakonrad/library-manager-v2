package pl.manager.library.database;

import pl.manager.library.model.Book;

import java.util.List;

public interface IRentalRepository {
    boolean rentBook(int bookId, int userId);
    boolean returnBook(int bookId, int userId);
    List<Book> getRentedBooksByUser(int userId);
}