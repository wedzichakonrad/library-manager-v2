package pl.manager.library.database;

import pl.manager.library.model.Book;

import java.util.List;

public interface IBookRepository {
    List<Book> getAllBooks();
    Book getBookById(int id);

    List<Book> findByAuthor(String author);
    List<Book> findByTitle(String title);

    void addBook(String author, String title);
    boolean deleteBook(int id);
    boolean updateBook(Book book);
}