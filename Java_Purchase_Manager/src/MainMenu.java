import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("Purchase Management System");
        setSize(400, 400); // Increased height for extra button(s)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Purchase Management System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Create buttons for each module
        JButton btnCustomers = new JButton("Customer Management");
        JButton btnProducts = new JButton("Product Management");
        JButton btnCategories = new JButton("Category Management");
        JButton btnInvoice = new JButton("Invoice Management");
        JButton btnTestConnection = new JButton("Test Database Connection");
        JButton btnExit = new JButton("Exit");
        
        // Add Action Listeners using our predefined methods
        btnCustomers.addActionListener(this::openCustomerManager);
        btnProducts.addActionListener(this::openProductManager);
        btnCategories.addActionListener(this::openCategoryManager);
        btnInvoice.addActionListener(this::openInvoiceManager);
        btnTestConnection.addActionListener(e -> DBConnection.testConnection());
        btnExit.addActionListener(e -> System.exit(0));
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(btnCustomers, gbc);
        
        gbc.gridy = 2;
        panel.add(btnProducts, gbc);
        
        gbc.gridy = 3;
        panel.add(btnCategories, gbc);
        
        gbc.gridy = 4;
        panel.add(btnInvoice, gbc);
        
        gbc.gridy = 5;
        panel.add(btnTestConnection, gbc);
        
        gbc.gridy = 6;
        panel.add(btnExit, gbc);
        
        add(panel);
    }
    
    private void openCustomerManager(ActionEvent e) {
        try {
            new CustomerManagerGUI().setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error opening Customer Manager: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openProductManager(ActionEvent e) {
        try {
            new ProductManagementGUI().setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error opening Product Manager: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openCategoryManager(ActionEvent e) {
        try {
            new CategoryManagerGUI().setVisible(true);
            dispose(); // Optionally close MainMenu
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error opening Category Manager: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openInvoiceManager(ActionEvent e) {
        try {
            new InvoiceManagerGUI().setVisible(true);
            dispose(); // Optionally close MainMenu
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error opening Invoice Manager: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MainMenu().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Failed to start application: " + e.getMessage(), 
                    "Fatal Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}


