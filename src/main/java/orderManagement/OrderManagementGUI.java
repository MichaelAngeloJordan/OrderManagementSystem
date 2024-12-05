package orderManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class OrderManagementGUI extends JFrame {
    private OrderDAO orderDAO;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JTextField filterField;
    private JComboBox<String> filterColumnComboBox;

    public OrderManagementGUI() {
        orderDAO = new OrderDAO();

        setTitle("Order Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Filter Panel
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        filterPanel.setBackground(new Color(230, 230, 250)); // Lavender

        JLabel filterLabel = new JLabel("Filter by:");
        filterLabel.setFont(new Font("Arial", Font.BOLD, 14));
        filterField = new JTextField();
        filterField.setPreferredSize(new Dimension(200, 25));
        filterColumnComboBox = new JComboBox<>(new String[]{"ID", "Customer Name", "Product Name", "Order Date"});
        JButton filterButton = new JButton("Apply Filter");

        filterButton.setBackground(new Color(70, 130, 180)); // Steel Blue
        filterButton.setForeground(Color.WHITE);
        filterButton.setFocusPainted(false);

        JPanel filterInputs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterInputs.setBackground(new Color(230, 230, 250));
        filterInputs.add(filterLabel);
        filterInputs.add(filterColumnComboBox);
        filterInputs.add(filterField);
        filterInputs.add(filterButton);

        filterPanel.add(filterInputs, BorderLayout.CENTER);
        add(filterPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"ID", "Customer Name", "Product Name", "Quantity", "Order Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        orderTable = new JTable(tableModel);

        orderTable.setRowHeight(25);
        orderTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JTableHeader header = orderTable.getTableHeader();
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(230, 230, 250));

        JButton addButton = new JButton("Add Order");
        JButton updateButton = new JButton("Update Order");
        JButton deleteButton = new JButton("Delete Order");
        JButton refreshButton = new JButton("Refresh");

        JButton[] buttons = {addButton, updateButton, deleteButton, refreshButton};
        for (JButton button : buttons) {
            button.setBackground(new Color(70, 130, 180));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setPreferredSize(new Dimension(120, 30));
        }

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
        filterButton.addActionListener(e -> applyFilter());

        refreshOrderTable();
    }

    private void applyFilter() {
        String keyword = filterField.getText().trim();
        int columnIndex = filterColumnComboBox.getSelectedIndex();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a keyword to filter.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Reset tabel sebelum menerapkan filter
        DefaultTableModel filteredModel = new DefaultTableModel(new String[]{"ID", "Customer Name", "Product Name", "Quantity", "Order Date"}, 0);
        List<Order> orders = orderDAO.getAllOrders();

        for (Order order : orders) {
            String columnValue;
            switch (columnIndex) {
                case 0: // Filter by ID
                    columnValue = String.valueOf(order.getId());
                    break;
                case 1: // Filter by Customer Name
                    columnValue = order.getCustomerName();
                    break;
                case 2: // Filter by Product Name
                    columnValue = order.getProductName();
                    break;
                case 3: // Filter by Order Date
                    columnValue = new SimpleDateFormat("yyyy-MM-dd").format(order.getOrderDate());
                    break;
                default:
                    columnValue = "";
            }

            // Periksa apakah kolom mengandung kata kunci (case-insensitive)
            if (columnValue.toLowerCase().contains(keyword.toLowerCase())) {
                filteredModel.addRow(new Object[]{
                        order.getId(),
                        order.getCustomerName(),
                        order.getProductName(),
                        order.getQuantity(),
                        new SimpleDateFormat("yyyy-MM-dd").format(order.getOrderDate())
                });
            }
        }

        // Tampilkan hasil filter pada tabel
        if (filteredModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No results found for the given filter.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
        orderTable.setModel(filteredModel);
    }

    private void refreshOrderTable() {
        tableModel.setRowCount(0);
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
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField customerNameField = new JTextField();
        JTextField productNameField = new JTextField();
        JTextField quantityField = new JTextField();

        panel.add(new JLabel("Customer Name:"));
        panel.add(customerNameField);
        panel.add(new JLabel("Product Name:"));
        panel.add(productNameField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Order", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String customerName = customerNameField.getText().trim();
                String productName = productNameField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());

                // Tanggal otomatis
                Date currentDate = new Date(System.currentTimeMillis());

                Order newOrder = new Order(0, customerName, productName, quantity, currentDate);
                orderDAO.insertOrder(newOrder); // Simpan ke database
                refreshOrderTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void showUpdateOrderDialog() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an order to update.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = (int) tableModel.getValueAt(selectedRow, 0);
        Order order = orderDAO.getAllOrders().stream()
                .filter(o -> o.getId() == orderId)
                .findFirst()
                .orElse(null);

        if (order == null) {
            JOptionPane.showMessageDialog(this, "Order not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10)); // Hanya 3 input
        JTextField customerNameField = new JTextField(order.getCustomerName());
        JTextField productNameField = new JTextField(order.getProductName());
        JTextField quantityField = new JTextField(String.valueOf(order.getQuantity()));

        panel.add(new JLabel("Customer Name:"));
        panel.add(customerNameField);
        panel.add(new JLabel("Product Name:"));
        panel.add(productNameField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Order", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                order.setCustomerName(customerNameField.getText().trim());
                order.setProductName(productNameField.getText().trim());
                order.setQuantity(Integer.parseInt(quantityField.getText().trim()));

                // Set tanggal otomatis
                Date currentDate = new Date(System.currentTimeMillis());
                order.setOrderDate(currentDate);

                orderDAO.updateOrder(order); // Update ke database
                refreshOrderTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void deleteOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an order to delete.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this order?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            orderDAO.deleteOrder(orderId); // Delete from database
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
