package pl.manager.library.database;

import org.springframework.stereotype.Component;
import pl.manager.library.config.DatabaseConfig;
import pl.manager.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookRepository implements IBookRepository {

    @Override
    public List<Book> getAllBooks() {
        return fetchBooks("SELECT * FROM books");
    }

    @Override
    public Book getBookById(int id) {
        List<Book> books = fetchBooksWithParam("SELECT * FROM books WHERE id = ?", id);
        return books.isEmpty() ? null : books.get(0);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return fetchBooksWithStringParam("SELECT * FROM books WHERE author LIKE ?", "%" + author + "%");
    }

    @Override
    public List<Book> findByTitle(String title) {
        return fetchBooksWithStringParam("SELECT * FROM books WHERE title LIKE ?", "%" + title + "%");
    }

    @Override
    public List<Book> findByCategory(int categoryId) {
        return fetchBooksWithParam("SELECT * FROM books WHERE category_id = ?", categoryId);
    }

    @Override
    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, year_published, category_id, is_available) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getYear());
            if (book.getCategoryId() != null && book.getCategoryId() > 0) {
                ps.setInt(4, book.getCategoryId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setBoolean(5, true);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteBook(int id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, year_published = ?, category_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getYear());
            if (book.getCategoryId() != null && book.getCategoryId() > 0) {
                ps.setInt(4, book.getCategoryId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setInt(5, book.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<Book> fetchBooks(String sql) {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) books.add(mapRowToBook(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    private List<Book> fetchBooksWithParam(String sql, int param) {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) books.add(mapRowToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    private List<Book> fetchBooksWithStringParam(String sql, String param) {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) books.add(mapRowToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    private Book mapRowToBook(ResultSet rs) throws SQLException {
        return Book.builder()
                .id(rs.getInt("id"))
                .title(rs.getString("title"))
                .author(rs.getString("author"))
                .year(rs.getInt("year_published"))
                .available(rs.getBoolean("is_available"))
                .categoryId(rs.getObject("category_id") != null ? rs.getInt("category_id") : null)
                .build();
    }
}