package orderManagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Method to insert a new order
    public void insertOrder(Order order) {
        if (order == null || order.getCustomerName() == null || order.getProductName() == null || order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid input parameters for inserting an order.");
        }

        String query = "INSERT INTO orders (customer_name, product_name, quantity, order_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = OrderDataBaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, order.getCustomerName());
            statement.setString(2, order.getProductName());
            statement.setInt(3, order.getQuantity());
            statement.setDate(4, new Date(System.currentTimeMillis())); // Default to current date
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order inserted successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error while inserting the order: " + e.getMessage());
        }
    }

    // Method to retrieve all orders
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders";
        try (Connection connection = OrderDataBaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String customerName = resultSet.getString("customer_name");
                String productName = resultSet.getString("product_name");
                int quantity = resultSet.getInt("quantity");
                Date orderDate = resultSet.getDate("order_date");
                orders.add(new Order(id, customerName, productName, quantity, orderDate));
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving orders: " + e.getMessage());
        }
        return orders;
    }

    // Method to update an existing order
    public void updateOrder(Order order) {
        if (order == null || order.getId() <= 0 || order.getCustomerName() == null ||
                order.getProductName() == null || order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid input parameters for updating an order.");
        }

        String query = "UPDATE orders SET customer_name = ?, product_name = ?, quantity = ?, order_date = ? WHERE id = ?";
        try (Connection connection = OrderDataBaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, order.getCustomerName());
            statement.setString(2, order.getProductName());
            statement.setInt(3, order.getQuantity());
            statement.setDate(4, new Date(System.currentTimeMillis())); // Update with current date
            statement.setInt(5, order.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order updated successfully.");
            } else {
                System.out.println("Order with ID " + order.getId() + " not found.");
            }
        } catch (SQLException e) {
            System.err.println("Error while updating the order: " + e.getMessage());
        }
    }

    // Method to delete an order by ID
    public void deleteOrder(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid order ID.");
        }

        String query = "DELETE FROM orders WHERE id = ?";
        try (Connection connection = OrderDataBaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order deleted successfully.");
            } else {
                System.out.println("Order with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            System.err.println("Error while deleting the order: " + e.getMessage());
        }
    }
}
