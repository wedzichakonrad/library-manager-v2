package pl.manager.library.database;

import org.springframework.stereotype.Component;
import pl.manager.library.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class BookRepository implements IBookRepository {
    private final List<Book> books = new ArrayList<>();
    private int bookId = 0;

    public BookRepository() {
        books.add(new Book(this.bookId++, "Lalka", "Bolesław Prus"));
        books.add(new Book(this.bookId++, "Rok 1984", "George Orwell"));
        books.add(new Book(this.bookId++, "Zbrodnia i Kara", "Fiodor Dostojewski"));
        books.add(new Book(this.bookId++, "Wiedzmin", "Andrzej Sapkowski"));
    }

    @Override
    public List<Book> getAllBooks() {
        return books;
    }

    @Override
    public Book getBookById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    private List<Book> findBy(Predicate<Book> condition) {
        return books.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return findBy(book -> book.getAuthor().equalsIgnoreCase(author));
    }

    @Override
    public List<Book> findByTitle(String title) {
        return findBy(book -> book.getTitle().equalsIgnoreCase(title));
    }

    @Override
    public void addBook(String author, String title) {
        books.add(new Book(this.bookId++, title, author));
    }

    @Override
    public boolean deleteBook(int id) {
        return books.removeIf(book -> book.getId() == id);
    }

    @Override
    public boolean updateBook(Book book) {
        Book bookToUpdate = getBookById(book.getId());
        if (bookToUpdate != null) {
            bookToUpdate.setTitle(book.getTitle());
            bookToUpdate.setAuthor(book.getAuthor());
            return true;
        }
        return false;
    }
}