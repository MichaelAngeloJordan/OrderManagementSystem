package orderManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
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

    private void applyFilter() {
        String filterText = filterField.getText().trim().toLowerCase();
        String selectedColumn = filterColumnComboBox.getSelectedItem().toString();

        if (filterText.isEmpty()) {
            refreshOrderTable();
            return;
        }

        List<Order> filteredOrders = orderDAO.getAllOrders().stream()
                .filter(order -> {
                    switch (selectedColumn) {
                        case "ID":
                            return String.valueOf(order.getId()).toLowerCase().contains(filterText);
                        case "Customer Name":
                            return order.getCustomerName().toLowerCase().contains(filterText);
                        case "Product Name":
                            return order.getProductName().toLowerCase().contains(filterText);
                        case "Order Date":
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String orderDate = dateFormat.format(order.getOrderDate());
                            return orderDate.contains(filterText);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());

        tableModel.setRowCount(0);
        for (Order order : filteredOrders) {
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
        // Your existing code for Add Order dialog
    }

    private void showUpdateOrderDialog() {
        // Your existing code for Update Order dialog
    }

    private void deleteOrder() {
        // Your existing code for Delete Order
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrderManagementGUI gui = new OrderManagementGUI();
            gui.setVisible(true);
        });
    }
}
