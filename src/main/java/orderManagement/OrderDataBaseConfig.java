package orderManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OrderDataBaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/ordermanagement";
    private static final String USER = "root";
    private static final String PASSWORD = "Untar123456";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection Updated");
            return connection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Main method untuk testing koneksi
    public static void main(String[] args) {
        getConnection();
    }
}
