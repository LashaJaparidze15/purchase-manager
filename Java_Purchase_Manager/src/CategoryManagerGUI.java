import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CategoryManagerGUI extends JFrame {
    private JTextField txtCategoryId, txtCategoryName, txtSearch;
    private JButton btnInsert, btnUpdate, btnDelete, btnRefresh, btnClear, btnSearch, btnBack;
    private JTable categoryTable;
    private DefaultTableModel tableModel;

    public CategoryManagerGUI() {
        initializeUI();
        setupTable();
        setupEventHandlers();
        refreshCategoryTable();
    }
    
    private void initializeUI() {
        setTitle("Category Manager");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBorder(new EmptyBorder(15,15,15,15));
        
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
            "Category Details",
            0, 0,
            new Font("SansSerif", Font.BOLD, 14),
            Color.DARK_GRAY));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 0: Category ID (read-only)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Category ID:"), gbc);
        gbc.gridx = 1;
        txtCategoryId = new JTextField(10);
        txtCategoryId.setEditable(false);
        formPanel.add(txtCategoryId, gbc);
        
        // Row 1: Category Name (required)
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Category Name*:"), gbc);
        gbc.gridx = 1;
        txtCategoryName = new JTextField(20);
        formPanel.add(txtCategoryName, gbc);
        
        // Row 2: Search Field
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Search:"), gbc);
        gbc.gridx = 1;
        txtSearch = new JTextField(20);
        formPanel.add(txtSearch, gbc);
        
        return formPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15,15));
        buttonPanel.setBackground(new Color(240,240,240));
        
        btnInsert = createStyledButton("Insert", new Color(0,100,0));
        btnUpdate = createStyledButton("Update", new Color(0,0,139));
        btnDelete = createStyledButton("Delete", new Color(139,0,0));
        btnRefresh = createStyledButton("Refresh", new Color(255,140,0));
        btnClear = createStyledButton("Clear", new Color(128,128,128));
        btnSearch = createStyledButton("Search", new Color(0,139,139));
        btnBack = createStyledButton("Back to Menu", new Color(139,0,139));
        
        buttonPanel.add(btnInsert);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnBack);
        
        return buttonPanel;
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        // Change text color as desired; here we use Black for contrast.
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(120, 30));
        return button;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"ID", "Category Name"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        categoryTable = new JTable(tableModel);
        categoryTable.setRowHeight(25);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Categories List"));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }
    
    private void setupTable() {
        categoryTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        categoryTable.getColumnModel().getColumn(1).setPreferredWidth(300); // Category Name
    }
    
    private void setupEventHandlers() {
        btnInsert.addActionListener(this::insertCategory);
        btnUpdate.addActionListener(this::updateCategory);
        btnDelete.addActionListener(this::deleteCategory);
        btnRefresh.addActionListener(e -> refreshCategoryTable());
        btnClear.addActionListener(e -> clearFields());
        btnSearch.addActionListener(this::searchCategory);
        btnBack.addActionListener(e -> {
            new MainMenu().setVisible(true);
            dispose();
        });
        
        categoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && categoryTable.getSelectedRow() >= 0) {
                int row = categoryTable.getSelectedRow();
                txtCategoryId.setText(tableModel.getValueAt(row, 0).toString());
                txtCategoryName.setText(tableModel.getValueAt(row, 1).toString());
            }
        });
    }
    
    private void insertCategory(ActionEvent e) {
        String catName = txtCategoryName.getText().trim();
        if (catName.isEmpty()) {
            showError("Category Name is required.");
            return;
        }
        boolean success = CategoryDAO.insertCategory(catName);
        if (success) {
            showSuccess("Category added successfully!");
            refreshCategoryTable();
            clearFields();
        } else {
            showError("Failed to add category. Please try again.");
        }
    }
    
    private void updateCategory(ActionEvent e) {
        String idStr = txtCategoryId.getText().trim();
        String catName = txtCategoryName.getText().trim();
        if (idStr.isEmpty() || catName.isEmpty()) {
            showError("Both ID and Category Name are required for update.");
            return;
        }
        try {
            int id = Integer.parseInt(idStr);
            CategoryDAO.Category category = new CategoryDAO.Category(id, catName);
            boolean success = CategoryDAO.updateCategory(category);
            if (success) {
                showSuccess("Category updated successfully!");
                refreshCategoryTable();
            } else {
                showError("Failed to update category.");
            }
        } catch (NumberFormatException ex) {
            showError("Invalid Category ID.");
        }
    }
    
    private void deleteCategory(ActionEvent e) {
        String idStr = txtCategoryId.getText().trim();
        if (idStr.isEmpty()) {
            showError("Please select a category to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this category?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idStr);
                boolean success = CategoryDAO.deleteCategory(id);
                if (success) {
                    showSuccess("Category deleted successfully!");
                    refreshCategoryTable();
                    clearFields();
                } else {
                    showError("Failed to delete category.");
                }
            } catch (NumberFormatException ex) {
                showError("Invalid Category ID.");
            }
        }
    }
    
    private void searchCategory(ActionEvent e) {
        String searchTerm = txtSearch.getText().trim();
        List<CategoryDAO.Category> categories = searchTerm.isEmpty() 
            ? CategoryDAO.getAllCategories() 
            : CategoryDAO.searchCategories(searchTerm);
        updateTable(categories);
    }
    
    private void refreshCategoryTable() {
        updateTable(CategoryDAO.getAllCategories());
    }
    
    private void updateTable(List<CategoryDAO.Category> categories) {
        tableModel.setRowCount(0);
        if (categories != null) {
            for (CategoryDAO.Category cat : categories) {
                tableModel.addRow(new Object[]{cat.getId(), cat.getName()});
            }
        }
    }
    
    private void clearFields() {
        txtCategoryId.setText("");
        txtCategoryName.setText("");
        txtSearch.setText("");
        categoryTable.clearSelection();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        // Apply Nimbus Look and Feel for a modern appearance
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Could not set Nimbus Look and Feel, using default.");
        }
        SwingUtilities.invokeLater(() -> new CategoryManagerGUI().setVisible(true));
    }
}