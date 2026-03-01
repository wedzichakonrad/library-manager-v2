package pl.manager.library.database;

import org.springframework.stereotype.Component;
import pl.manager.library.config.DatabaseConfig;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class StatisticsRepository implements IStatisticsRepository {

    @Override
    public Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new LinkedHashMap<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM books");
            if (rs.next()) stats.put("Total Books", rs.getInt(1));

            rs = stmt.executeQuery("SELECT COUNT(*) FROM books WHERE is_available = TRUE");
            if (rs.next()) stats.put("Available Books", rs.getInt(1));

            rs = stmt.executeQuery("SELECT COUNT(*) FROM books WHERE is_available = FALSE");
            if (rs.next()) stats.put("Rented Books", rs.getInt(1));

            rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.next()) stats.put("Total Users", rs.getInt(1));

            rs = stmt.executeQuery("SELECT COUNT(*) FROM rentals WHERE is_active = TRUE");
            if (rs.next()) stats.put("Active Rentals", rs.getInt(1));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }

    @Override
    public Map<String, Integer> getMostPopularBooks(int limit) {
        Map<String, Integer> popularBooks = new LinkedHashMap<>();
        String sql = "SELECT b.title, COUNT(r.id) as rental_count " +
                "FROM books b JOIN rentals r ON b.id = r.book_id " +
                "GROUP BY b.id, b.title " +
                "ORDER BY rental_count DESC " +
                "LIMIT ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    popularBooks.put(rs.getString("title"), rs.getInt("rental_count"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return popularBooks;
    }
}