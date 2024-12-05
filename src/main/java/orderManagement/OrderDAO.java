package orderManagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    // Query SQL sebagai konstanta
    private static final String INSERT_ORDER_QUERY = "INSERT INTO orders (customer_name, product_name, quantity, order_date) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_ORDERS_QUERY = "SELECT * FROM orders";
    private static final String UPDATE_ORDER_QUERY = "UPDATE orders SET customer_name = ?, product_name = ?, quantity = ?, order_date = ? WHERE id = ?";
    private static final String DELETE_ORDER_QUERY = "DELETE FROM orders WHERE id = ?";

    // Method untuk menambahkan order baru
    public void insertOrder(Order order) {
        if (order == null || order.getCustomerName() == null || order.getProductName() == null || order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Parameter input tidak valid untuk menambahkan order.");
        }

        try (Connection connection = OrderDataBaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_ORDER_QUERY)) {

            statement.setString(1, order.getCustomerName());
            statement.setString(2, order.getProductName());
            statement.setInt(3, order.getQuantity());
            statement.setDate(4, new Date(System.currentTimeMillis())); // Set tanggal sekarang
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Order berhasil ditambahkan.");
            } else {
                System.err.println("Gagal menambahkan order.");
            }
        } catch (SQLException e) {
            System.err.println("Kesalahan saat menambahkan order: " + e.getMessage());
        }
    }

    // Method untuk mengambil semua data order
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = OrderDataBaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_ORDERS_QUERY)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String customerName = resultSet.getString("customer_name");
                String productName = resultSet.getString("product_name");
                int quantity = resultSet.getInt("quantity");
                Date orderDate = resultSet.getDate("order_date");

                orders.add(new Order(id, customerName, productName, quantity, orderDate));
            }
            System.out.println("Data retrieved: " + orders.size() + " orders.");
        } catch (SQLException e) {
            System.err.println("Kesalahan saat mengambil data order: " + e.getMessage());
        }

        return orders;
    }


    // Method untuk memperbarui order
    public void updateOrder(Order order) {
        if (order == null || order.getId() <= 0 || order.getCustomerName() == null ||
                order.getProductName() == null || order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Parameter input tidak valid untuk memperbarui order.");
        }

        try (Connection connection = OrderDataBaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER_QUERY)) {

            statement.setString(1, order.getCustomerName());
            statement.setString(2, order.getProductName());
            statement.setInt(3, order.getQuantity());
            statement.setDate(4, new Date(System.currentTimeMillis())); // Perbarui dengan tanggal sekarang
            statement.setInt(5, order.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order berhasil diperbarui.");
            } else {
                System.out.println("Order dengan ID " + order.getId() + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.err.println("Kesalahan saat memperbarui order: " + e.getMessage());
        }
    }

    // Method untuk menghapus order berdasarkan ID
    public void deleteOrder(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID order tidak valid.");
        }

        try (Connection connection = OrderDataBaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ORDER_QUERY)) {

            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order berhasil dihapus.");
            } else {
                System.out.println("Order dengan ID " + id + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            System.err.println("Kesalahan saat menghapus order: " + e.getMessage());
        }
    }
}
