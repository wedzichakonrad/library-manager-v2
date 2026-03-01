package pl.manager.library.config;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class DatabaseConfig {
    private static final String URL = "jdbc:h2:./library_db;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void initialize() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            var is = DatabaseConfig.class.getClassLoader().getResourceAsStream("schema.sql");
            if (is == null) {
                System.err.println("schema.sql Not found");
                return;
            }

            String sql = new String(is.readAllBytes());
            stmt.execute(sql);

            String resetAdminSql = "UPDATE users SET password = ? WHERE username = 'admin'";
            try (PreparedStatement ps = conn.prepareStatement(resetAdminSql)) {
                ps.setString(1, BCrypt.hashpw("admin", BCrypt.gensalt()));
                ps.executeUpdate();
            }

            System.out.println("Database ready, admin password and username reset");

        } catch (Exception e) {
            System.err.println("Initialization error: " + e.getMessage());
        }
    }
}