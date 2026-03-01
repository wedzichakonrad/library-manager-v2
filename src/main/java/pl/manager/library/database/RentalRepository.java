package pl.manager.library.database;

import org.springframework.stereotype.Component;
import pl.manager.library.config.DatabaseConfig;
import pl.manager.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class RentalRepository implements IRentalRepository {

    @Override
    public boolean rentBook(int bookId, int userId) {
        String checkSql = "SELECT is_available FROM books WHERE id = ?";
        String rentSql = "INSERT INTO rentals (book_id, user_id) VALUES (?, ?)";
        String updateBookSql = "UPDATE books SET is_available = FALSE WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, bookId);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next() || !rs.getBoolean("is_available")) {
                    return false;
                }
            }

            try (PreparedStatement rentStmt = conn.prepareStatement(rentSql);
                 PreparedStatement updateStmt = conn.prepareStatement(updateBookSql)) {

                rentStmt.setInt(1, bookId);
                rentStmt.setInt(2, userId);
                rentStmt.executeUpdate();

                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean returnBook(int bookId, int userId) {
        String checkSql = "SELECT id FROM rentals WHERE book_id = ? AND user_id = ? AND is_active = TRUE";
        String returnSql = "UPDATE rentals SET is_active = FALSE WHERE id = ?";
        String updateBookSql = "UPDATE books SET is_available = TRUE WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            int rentalId = -1;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, bookId);
                checkStmt.setInt(2, userId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    rentalId = rs.getInt("id");
                } else {
                    return false;
                }
            }

            try (PreparedStatement returnStmt = conn.prepareStatement(returnSql);
                 PreparedStatement updateStmt = conn.prepareStatement(updateBookSql)) {

                returnStmt.setInt(1, rentalId);
                returnStmt.executeUpdate();

                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Book> getRentedBooksByUser(int userId) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.* FROM books b JOIN rentals r ON b.id = r.book_id WHERE r.user_id = ? AND r.is_active = TRUE";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(Book.builder()
                            .id(rs.getInt("id"))
                            .title(rs.getString("title"))
                            .author(rs.getString("author"))
                            .year(rs.getInt("year_published"))
                            .available(rs.getBoolean("is_available"))
                            .categoryId(rs.getObject("category_id") != null ? rs.getInt("category_id") : null)
                            .build());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}