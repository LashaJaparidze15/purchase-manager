import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class InvoiceManagerGUI extends JFrame {
    private JComboBox<String> customerComboBox;
    private JTextField txtProductId, txtQuantity, txtSearch;
    private JButton btnAddItem, btnCreateInvoice, btnClearItems, btnRefresh, btnBack;
    private JTable itemTable;
    private DefaultTableModel itemTableModel;
    private JLabel lblTotal;
    
    private List<InvoiceItem> invoiceItems = new ArrayList<>();
    
    public InvoiceManagerGUI() {
        initializeUI();
        setupEventHandlers();
    }
    
    private void initializeUI() {
        setTitle("Invoice Manager");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.NORTH);

        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY),
            "Invoice Details",
            0, 0,
            new Font("SansSerif", Font.BOLD, 14),
            Color.DARK_GRAY));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Customer:"), gbc);
        gbc.gridx = 1;
        customerComboBox = new JComboBox<>();
        customerComboBox.addItem("1: John Doe");
        customerComboBox.addItem("2: Jane Smith");
        customerComboBox.addItem("3: Bob Johnson");
        formPanel.add(customerComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Product ID*:"), gbc);
        gbc.gridx = 1;
        txtProductId = new JTextField(10);
        formPanel.add(txtProductId, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Quantity*:"), gbc);
        gbc.gridx = 1;
        txtQuantity = new JTextField(10);
        formPanel.add(txtQuantity, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Search:"), gbc);
        gbc.gridx = 1;
        txtSearch = new JTextField(20);
        formPanel.add(txtSearch, gbc);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(240, 240, 240));

        btnAddItem = createStyledButton("Add Item", new Color(0, 100, 0));
        btnCreateInvoice = createStyledButton("Create Invoice", new Color(0, 0, 139));
        btnClearItems = createStyledButton("Clear Items", new Color(139, 0, 0));
        btnRefresh = createStyledButton("Refresh", new Color(255, 140, 0));
        btnBack = createStyledButton("Back to Menu", new Color(139, 0, 139));

        buttonPanel.add(btnAddItem);
        buttonPanel.add(btnCreateInvoice);
        buttonPanel.add(btnClearItems);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 30));
        return button;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        
        itemTableModel = new DefaultTableModel(new Object[]{"Product ID", "Quantity", "Unit Price", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        itemTable = new JTable(itemTableModel);
        itemTable.setRowHeight(25);
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(itemTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Invoice Items"));
        
        // Total label at bottom of table panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalPanel.add(lblTotal);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(totalPanel, BorderLayout.SOUTH);
        
        return tablePanel;
    }

    private void setupEventHandlers() {
        btnAddItem.addActionListener(this::addInvoiceItem);
        btnCreateInvoice.addActionListener(this::createInvoice);
        btnClearItems.addActionListener(e -> clearInvoiceItems());
        btnRefresh.addActionListener(e -> refreshInvoiceItems());
        btnBack.addActionListener(e -> {
            new MainMenu().setVisible(true);
            dispose();
        });
    }
    
    private void addInvoiceItem(ActionEvent e) {
        String prodIdStr = txtProductId.getText().trim();
        String qtyStr = txtQuantity.getText().trim();
        if (prodIdStr.isEmpty() || qtyStr.isEmpty()) {
            showError("Please enter Product ID and Quantity (marked with *)");
            return;
        }
        try {
            int prodId = Integer.parseInt(prodIdStr);
            int quantity = Integer.parseInt(qtyStr);
            if (quantity <= 0) {
                showError("Quantity must be greater than 0");
                return;
            }
            
            double unitPrice = 29.99; // Replace with actual product lookup
            
            InvoiceItem item = new InvoiceItem(prodId, quantity, unitPrice);
            invoiceItems.add(item);
            double subtotal = item.getSubtotal();
            itemTableModel.addRow(new Object[]{prodId, quantity, unitPrice, subtotal});
            updateTotal();
            txtProductId.setText("");
            txtQuantity.setText("");
        } catch (NumberFormatException ex) {
            showError("Product ID and Quantity must be numeric");
        }
    }
    
    private void createInvoice(ActionEvent e) {
        String customerInfo = (String) customerComboBox.getSelectedItem();
        if (customerInfo == null || customerInfo.isEmpty()) {
            showError("Please select a customer");
            return;
        }
        
        int customerId = Integer.parseInt(customerInfo.split(":")[0].trim());
        if (invoiceItems.isEmpty()) {
            showError("No invoice items added");
            return;
        }
        
        int invoiceId = InvoiceDAO.createInvoice(customerId, invoiceItems);
        if (invoiceId != -1) {
            showSuccess("Invoice created successfully! Invoice ID: " + invoiceId);
            clearInvoiceItems();
        } else {
            showError("Failed to create invoice");
        }
    }
    
    private void refreshInvoiceItems() {
        // In a real implementation, this might refresh available products or customers
        JOptionPane.showMessageDialog(this, "Refreshing data...");
    }
    
    private void clearInvoiceItems() {
        invoiceItems.clear();
        itemTableModel.setRowCount(0);
        updateTotal();
    }
    
    private void updateTotal() {
        double total = 0.0;
        for (InvoiceItem item : invoiceItems) {
            total += item.getSubtotal();
        }
        lblTotal.setText(String.format("Total: $%.2f", total));
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Nimbus Look and Feel not set, using default.");
        }
        SwingUtilities.invokeLater(() -> new InvoiceManagerGUI().setVisible(true));
    }
}