package orderManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class OrderManagementGUI extends JFrame {
    private OrderDAO orderDAO;
    private JTable orderTable;
    private DefaultTableModel tableModel;

    public OrderManagementGUI() {
        orderDAO = new OrderDAO();

        setTitle("Order Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // For Table
        String[] columnNames = {"ID", "Customer Name", "Product Name", "Quantity", "Order Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        // For Button
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Order");
        JButton updateButton = new JButton("Update Order");
        JButton deleteButton = new JButton("Delete Order");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event listeners
        addButton.addActionListener(e -> showAddOrderDialog());
        updateButton.addActionListener(e -> showUpdateOrderDialog());
        deleteButton.addActionListener(e -> deleteOrder());
        refreshButton.addActionListener(e -> refreshOrderTable());
        refreshOrderTable();
    }

    private void refreshOrderTable() {
        tableModel.setRowCount(0); // Clear existing rows
        List<Order> orders = orderDAO.getAllOrders();
        for (Order order : orders) {
            tableModel.addRow(new Object[]{
                    order.getId(),
                    order.getCustomerName(),
                    order.getProductName(),
                    order.getQuantity(),
                    order.getOrderDate()
            });
        }
    }

    private void showAddOrderDialog() {
        JDialog dialog = new JDialog(this, "Add Order", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(5, 2));

        JTextField customerNameField = new JTextField();
        JTextField productNameField = new JTextField();
        JTextField quantityField = new JTextField();

        dialog.add(new JLabel("Customer Name:"));
        dialog.add(customerNameField);
        dialog.add(new JLabel("Product Name:"));
        dialog.add(productNameField);
        dialog.add(new JLabel("Quantity:"));
        dialog.add(quantityField);

        JButton addButton = new JButton("Add");
        dialog.add(addButton);

        addButton.addActionListener(e -> {
            try {
                String customerName = customerNameField.getText();
                String productName = productNameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());

                Order order = new Order(0, customerName, productName, quantity, null);
                orderDAO.insertOrder(order);
                refreshOrderTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private void showUpdateOrderDialog() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order to update.");
            return;
        }

        int orderId = (int) tableModel.getValueAt(selectedRow, 0);

        JDialog dialog = new JDialog(this, "Update Order", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(5, 2));

        JTextField customerNameField = new JTextField((String) tableModel.getValueAt(selectedRow, 1));
        JTextField productNameField = new JTextField((String) tableModel.getValueAt(selectedRow, 2));
        JTextField quantityField = new JTextField(String.valueOf(tableModel.getValueAt(selectedRow, 3)));

        dialog.add(new JLabel("Customer Name:"));
        dialog.add(customerNameField);
        dialog.add(new JLabel("Product Name:"));
        dialog.add(productNameField);
        dialog.add(new JLabel("Quantity:"));
        dialog.add(quantityField);

        JButton updateButton = new JButton("Update");
        dialog.add(updateButton);

        updateButton.addActionListener(e -> {
            try {
                String customerName = customerNameField.getText();
                String productName = productNameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());

                Order order = new Order(orderId, customerName, productName, quantity, null);
                orderDAO.updateOrder(order);
                refreshOrderTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private void deleteOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order to delete.");
            return;
        }

        int orderId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this order?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            orderDAO.deleteOrder(orderId);
            refreshOrderTable();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrderManagementGUI gui = new OrderManagementGUI();
            gui.setVisible(true);
        });
    }
}
