import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CustomerManagerGUI extends JFrame {
    private JTextField txtCustomerId, txtName, txtEmail, txtPhone, txtSearch;
    private JButton btnInsert, btnUpdate, btnDelete, btnRefresh, btnClear, btnSearch, btnBack;
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public CustomerManagerGUI() {
        initializeUI();
        setupTable();
        setupEventHandlers();
        refreshCustomerTable();
    }

    private void initializeUI() {
        setTitle("Customer Manager");
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
            "Customer Details",
            0, 0,
            new Font("SansSerif", Font.BOLD, 14),
            Color.DARK_GRAY));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1;
        txtCustomerId = new JTextField(10);
        txtCustomerId.setEditable(false);
        formPanel.add(txtCustomerId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Name*:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(20);
        formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email*:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(25);
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Phone*:"), gbc);
        gbc.gridx = 1;
        txtPhone = new JTextField(15);
        formPanel.add(txtPhone, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
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
        
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Email", "Phone", "Created"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        customerTable = new JTable(tableModel);
        customerTable.setRowHeight(25);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Customer List"));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private void setupTable() {
        customerTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        customerTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        customerTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Email
        customerTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Phone
        customerTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Created
    }

    private void setupEventHandlers() {
        btnInsert.addActionListener(this::insertCustomer);
        btnUpdate.addActionListener(this::updateCustomer);
        btnDelete.addActionListener(this::deleteCustomer);
        btnRefresh.addActionListener(e -> refreshCustomerTable());
        btnClear.addActionListener(e -> clearFields());
        btnSearch.addActionListener(this::searchCustomer);
        btnBack.addActionListener(e -> {
            new MainMenu().setVisible(true);
            dispose();
        });

        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && customerTable.getSelectedRow() >= 0) {
                int row = customerTable.getSelectedRow();
                txtCustomerId.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                txtEmail.setText(tableModel.getValueAt(row, 2).toString());
                txtPhone.setText(tableModel.getValueAt(row, 3).toString());
            }
        });
    }

    private void insertCustomer(ActionEvent e) {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showError("Please fill in all required fields (marked with *)");
            return;
        }

        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showError("Please enter a valid email address");
            return;
        }

        if (!phone.matches("^\\+?[0-9\\s-]{6,20}$")) {
            showError("Please enter a valid phone number");
            return;
        }

        boolean success = CustomerDAO.insertCustomer(name, email, phone);
        if (success) {
            showSuccess("Customer added successfully!");
            refreshCustomerTable();
            clearFields();
        } else {
            showError("Failed to add customer. Please try again.");
        }
    }

    private void updateCustomer(ActionEvent e) {
        String idStr = txtCustomerId.getText().trim();
        if (idStr.isEmpty()) {
            showError("Please select a customer to update");
            return;
        }

        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showError("Please fill in all required fields (marked with *)");
            return;
        }

        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showError("Please enter a valid email address");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            CustomerDAO.Customer customer = new CustomerDAO.Customer(
                id, name, email, phone, null
            );
            
            boolean success = CustomerDAO.updateCustomer(customer);
            if (success) {
                showSuccess("Customer updated successfully!");
                refreshCustomerTable();
            } else {
                showError("Failed to update customer. Please try again.");
            }
        } catch (NumberFormatException ex) {
            showError("Invalid customer ID");
        }
    }

    private void deleteCustomer(ActionEvent e) {
        String idStr = txtCustomerId.getText().trim();
        if (idStr.isEmpty()) {
            showError("Please select a customer to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this customer?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idStr);
                boolean success = CustomerDAO.deleteCustomer(id);
                if (success) {
                    showSuccess("Customer deleted successfully!");
                    refreshCustomerTable();
                    clearFields();
                } else {
                    showError("Failed to delete customer. Please try again.");
                }
            } catch (NumberFormatException ex) {
                showError("Invalid customer ID");
            }
        }
    }

    private void searchCustomer(ActionEvent e) {
        String searchTerm = txtSearch.getText().trim();
        List<CustomerDAO.Customer> customers = searchTerm.isEmpty() 
            ? CustomerDAO.getAllCustomers() 
            : CustomerDAO.searchCustomers(searchTerm);
        updateTable(customers);
    }

    private void refreshCustomerTable() {
        updateTable(CustomerDAO.getAllCustomers());
    }

    private void updateTable(List<CustomerDAO.Customer> customers) {
        tableModel.setRowCount(0);
        if (customers != null) {
            for (CustomerDAO.Customer customer : customers) {
                tableModel.addRow(new Object[]{
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getCreatedAt()
                });
            }
        }
    }

    private void clearFields() {
        txtCustomerId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtSearch.setText("");
        customerTable.clearSelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
