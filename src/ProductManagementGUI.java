import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class ProductManagementGUI extends JFrame {
    private JTextField txtProductId, txtProductName, txtPrice, txtStock, txtCategoryId, txtCategoryName, txtSearch;
    private JButton btnInsert, btnUpdate, btnDelete, btnRefresh, btnClear, btnSearch, btnBack;
    private JTable productTable;
    private DefaultTableModel tableModel;

    public ProductManagementGUI() {
        initializeUI();
        setupTable();
        setupEventHandlers();
        refreshProductTable();
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                DBConnection.closeConnection();
            }
        });
    }

    private void initializeUI() {
        setTitle("Product Management");
        setSize(1100, 700);
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
            "Product Details",
            0, 0,
            new Font("SansSerif", Font.BOLD, 14),
            Color.DARK_GRAY));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Product ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 1;
        txtProductId = new JTextField(10);
        txtProductId.setEditable(false);
        formPanel.add(txtProductId, gbc);

        // Row 1: Name (required)
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Name*:"), gbc);
        gbc.gridx = 1;
        txtProductName = new JTextField(20);
        formPanel.add(txtProductName, gbc);

        // Row 2: Price (required)
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Price*:"), gbc);
        gbc.gridx = 1;
        txtPrice = new JTextField(10);
        formPanel.add(txtPrice, gbc);

        // Row 3: Stock (required)
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Stock*:"), gbc);
        gbc.gridx = 1;
        txtStock = new JTextField(10);
        formPanel.add(txtStock, gbc);

        // Row 4: Category ID (required)
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Category ID*:"), gbc);
        gbc.gridx = 1;
        txtCategoryId = new JTextField(10);
        formPanel.add(txtCategoryId, gbc);

        // Row 5: Category Name (display only)
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Category Name:"), gbc);
        gbc.gridx = 1;
        txtCategoryName = new JTextField(15);
        txtCategoryName.setEditable(false);
        formPanel.add(txtCategoryName, gbc);

        // Row 6: Search field
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Search:"), gbc);
        gbc.gridx = 1;
        txtSearch = new JTextField(20);
        formPanel.add(txtSearch, gbc);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(240, 240, 240));

        btnInsert = createStyledButton("Insert", new Color(0, 100, 0));
        btnUpdate = createStyledButton("Update", new Color(0, 0, 139));
        btnDelete = createStyledButton("Delete", new Color(139, 0, 0));
        btnRefresh = createStyledButton("Refresh", new Color(255, 140, 0));
        btnClear = createStyledButton("Clear", new Color(128, 128, 128));
        btnSearch = createStyledButton("Search", new Color(0, 139, 139));
        btnBack = createStyledButton("Back to Menu", new Color(139, 0, 139));

        buttonPanel.add(btnInsert);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnSearch);
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
        
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Price", "Stock", "Category ID", "Category Name"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        productTable = new JTable(tableModel);
        productTable.setRowHeight(25);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Product List"));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private void setupTable() {
        productTable.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        productTable.getColumnModel().getColumn(1).setPreferredWidth(200);  // Name
        productTable.getColumnModel().getColumn(2).setPreferredWidth(100);  // Price
        productTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Stock
        productTable.getColumnModel().getColumn(4).setPreferredWidth(100);  // Category ID
        productTable.getColumnModel().getColumn(5).setPreferredWidth(200);  // Category Name
    }

    private void setupEventHandlers() {
        btnInsert.addActionListener(this::insertProduct);
        btnUpdate.addActionListener(this::updateProduct);
        btnDelete.addActionListener(this::deleteProduct);
        btnRefresh.addActionListener(e -> refreshProductTable());
        btnClear.addActionListener(e -> clearFields());
        btnSearch.addActionListener(this::searchProduct);
        btnBack.addActionListener(e -> {
            new MainMenu().setVisible(true);
            dispose();
        });

        // When a row in the table is selected, populate the form fields with the product details.
        ListSelectionListener selectionListener = e -> {
            if (!e.getValueIsAdjusting() && productTable.getSelectedRow() >= 0) {
                int row = productTable.getSelectedRow();
                txtProductId.setText(tableModel.getValueAt(row, 0).toString());
                txtProductName.setText(tableModel.getValueAt(row, 1).toString());
                txtPrice.setText(tableModel.getValueAt(row, 2).toString());
                txtStock.setText(tableModel.getValueAt(row, 3).toString());
                txtCategoryId.setText(tableModel.getValueAt(row, 4).toString());
                txtCategoryName.setText(tableModel.getValueAt(row, 5).toString());
            }
        };
        productTable.getSelectionModel().addListSelectionListener(selectionListener);
    }

    private void insertProduct(ActionEvent e) {
        String name = txtProductName.getText().trim();
        String priceStr = txtPrice.getText().trim();
        String stockStr = txtStock.getText().trim();
        String categoryIdStr = txtCategoryId.getText().trim();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() || categoryIdStr.isEmpty()) {
            showError("Please fill in all required fields (marked with *)");
            return;
        }

        double price;
        int stock, categoryId;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                showError("Price must be greater than 0");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid price");
            return;
        }

        try {
            stock = Integer.parseInt(stockStr);
            if (stock < 0) {
                showError("Stock cannot be negative");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid stock quantity");
            return;
        }

        try {
            categoryId = Integer.parseInt(categoryIdStr);
            if (categoryId <= 0) {
                showError("Category ID must be greater than 0");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid Category ID");
            return;
        }

        boolean success = ProductDAO.insertProduct(name, price, stock, categoryId);
        if (success) {
            showSuccess("Product added successfully!");
            refreshProductTable();
            clearFields();
        } else {
            showError("Failed to add product. Please try again.");
        }
    }

    private void updateProduct(ActionEvent e) {
        String idStr = txtProductId.getText().trim();
        if (idStr.isEmpty()) {
            showError("Please select a product to update");
            return;
        }

        String name = txtProductName.getText().trim();
        String priceStr = txtPrice.getText().trim();
        String stockStr = txtStock.getText().trim();
        String categoryIdStr = txtCategoryId.getText().trim();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() || categoryIdStr.isEmpty()) {
            showError("Please fill in all required fields (marked with *)");
            return;
        }

        double price;
        int stock, categoryId;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                showError("Price must be greater than 0");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid price");
            return;
        }

        try {
            stock = Integer.parseInt(stockStr);
            if (stock < 0) {
                showError("Stock cannot be negative");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid stock quantity");
            return;
        }

        try {
            categoryId = Integer.parseInt(categoryIdStr);
            if (categoryId <= 0) {
                showError("Category ID must be greater than 0");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Please enter a valid Category ID");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            // Create a Product instance; category name is fetched from the form.
            ProductDAO.Product product = new ProductDAO.Product(
                id, name, price, stock, categoryId, txtCategoryName.getText().trim()
            );
            
            boolean success = ProductDAO.updateProduct(product);
            if (success) {
                showSuccess("Product updated successfully!");
                refreshProductTable();
            } else {
                showError("Failed to update product. Please try again.");
            }
        } catch (NumberFormatException ex) {
            showError("Invalid product ID");
        }
    }

    private void deleteProduct(ActionEvent e) {
        String idStr = txtProductId.getText().trim();
        if (idStr.isEmpty()) {
            showError("Please select a product to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this product?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idStr);
                boolean success = ProductDAO.deleteProduct(id);
                if (success) {
                    showSuccess("Product deleted successfully!");
                    refreshProductTable();
                    clearFields();
                } else {
                    showError("Failed to delete product. Please try again.");
                }
            } catch (NumberFormatException ex) {
                showError("Invalid product ID");
            }
        }
    }

    private void searchProduct(ActionEvent e) {
        String searchTerm = txtSearch.getText().trim();
        List<ProductDAO.Product> products = searchTerm.isEmpty() 
            ? ProductDAO.getAllProducts() 
            : ProductDAO.searchProducts(searchTerm);
        updateTable(products);
    }

    private void refreshProductTable() {
        updateTable(ProductDAO.getAllProducts());
    }

    private void updateTable(List<ProductDAO.Product> products) {
        tableModel.setRowCount(0);
        if (products != null) {
            for (ProductDAO.Product product : products) {
                tableModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getStock(),
                    product.getCategoryId(),
                    product.getCategoryName()
                });
            }
        }
    }

    private void clearFields() {
        txtProductId.setText("");
        txtProductName.setText("");
        txtPrice.setText("");
        txtStock.setText("");
        txtCategoryId.setText("");
        txtCategoryName.setText("");
        txtSearch.setText("");
        productTable.clearSelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}