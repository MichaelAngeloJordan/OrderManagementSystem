package orderManagement;

import java.util.List;
import java.util.Scanner;

public class OrderManagementApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OrderDAO orderDAO = new OrderDAO();

        System.out.println("=== Welcome to the Order Management System ===");

        while (true) {
            System.out.println("\n=== Menu ===");
            System.out.println("1. Add Order");
            System.out.println("2. View All Orders");
            System.out.println("3. Update Order");
            System.out.println("4. Delete Order");
            System.out.println("5. Exit");
            System.out.print("Choose an option (1-5): ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                continue;
            }

            switch (choice) {
                case 1 -> addOrder(scanner, orderDAO);
                case 2 -> viewAllOrders(orderDAO);
                case 3 -> updateOrder(scanner, orderDAO);
                case 4 -> deleteOrder(scanner, orderDAO);
                case 5 -> {
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addOrder(Scanner scanner, OrderDAO orderDAO) {
        try {
            System.out.print("Enter Customer Name: ");
            String customerName = scanner.nextLine().trim();
            if (customerName.isEmpty()) {
                System.out.println("Customer name cannot be empty.");
                return;
            }

            System.out.print("Enter Product Name: ");
            String productName = scanner.nextLine().trim();
            if (productName.isEmpty()) {
                System.out.println("Product name cannot be empty.");
                return;
            }

            System.out.print("Enter Quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());
            if (quantity <= 0) {
                System.out.println("Quantity must be a positive number.");
                return;
            }

            Order order = new Order(0, customerName, productName, quantity, null);
            orderDAO.insertOrder(order);

            System.out.println("Order added successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for quantity. Please enter a valid number.");
        }
    }

    private static void viewAllOrders(OrderDAO orderDAO) {
        List<Order> orders = orderDAO.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            System.out.println("\n=== Order List ===");
            for (Order order : orders) {
                System.out.println(order);
            }
        }
    }

    private static void updateOrder(Scanner scanner, OrderDAO orderDAO) {
        try {
            System.out.print("Enter Order ID to update: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter New Customer Name: ");
            String customerName = scanner.nextLine().trim();
            if (customerName.isEmpty()) {
                System.out.println("Customer name cannot be empty.");
                return;
            }

            System.out.print("Enter New Product Name: ");
            String productName = scanner.nextLine().trim();
            if (productName.isEmpty()) {
                System.out.println("Product name cannot be empty.");
                return;
            }

            System.out.print("Enter New Quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());
            if (quantity <= 0) {
                System.out.println("Quantity must be a positive number.");
                return;
            }

            Order updatedOrder = new Order(id, customerName, productName, quantity, null);
            orderDAO.updateOrder(updatedOrder);

            System.out.println("Order updated successfully!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numbers.");
        }
    }

    private static void deleteOrder(Scanner scanner, OrderDAO orderDAO) {
        try {
            System.out.print("Enter Order ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine());

            orderDAO.deleteOrder(id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }
}
