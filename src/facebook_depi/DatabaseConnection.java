package facebook_depi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Define the connection URL, username, and password
    private static final String URL = "jdbc:mysql://localhost:3306/facebookdebi";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    // Method to establish a connection to the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Optionally, you can test the connection
    public static void testConnection() {
        try (Connection connection = getConnection()) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}
