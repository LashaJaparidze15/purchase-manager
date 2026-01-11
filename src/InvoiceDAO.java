import java.sql.*;
import java.util.List;

public class InvoiceDAO {

    /**
     * Creates a new invoice for the given customer and list of invoice items.
     * Returns the generated invoice_id, or -1 if the operation fails.
     */
    public static int createInvoice(int customerId, List<InvoiceItem> items) {
        int invoiceId = -1;
        Connection conn = null;
        try {
            conn = DBConnection.connect();
            if (conn == null) throw new SQLException("Unable to obtain database connection.");
            
            conn.setAutoCommit(false);  // Start transaction

            // Insert into invoice table with total_amount 0 initially.
            String invoiceSQL = "INSERT INTO Invoice (customer_id, total_amount) VALUES (?, 0)";
            try (PreparedStatement pstmt = conn.prepareStatement(invoiceSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, customerId);
                int affected = pstmt.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Creating invoice failed, no rows affected.");
                }
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        invoiceId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating invoice failed, no ID obtained.");
                    }
                }
            }

            double totalAmount = 0.0;

            // Insert each invoice item and update product stock.
            String invoiceItemSQL = "INSERT INTO InvoiceItem (invoice_id, product_id, quantity, subtotal) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(invoiceItemSQL)) {
                for (InvoiceItem item : items) {
                    double subtotal = item.getSubtotal();
                    totalAmount += subtotal;
                    
                    pstmt.setInt(1, invoiceId);
                    pstmt.setInt(2, item.getProductId());
                    pstmt.setInt(3, item.getQuantity());
                    pstmt.setDouble(4, subtotal);
                    pstmt.executeUpdate();
                    
                    // Update product stock: subtract the quantity purchased.
                    updateProductStock(item.getProductId(), item.getQuantity(), conn);
                }
            }

            // Update the invoice record with the total amount.
            String updateInvoiceSQL = "UPDATE Invoice SET total_amount = ? WHERE invoice_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateInvoiceSQL)) {
                pstmt.setDouble(1, totalAmount);
                pstmt.setInt(2, invoiceId);
                pstmt.executeUpdate();
            }

            conn.commit();  // Commit transaction
        } catch (SQLException e) {
            System.err.println("Error creating invoice: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
            invoiceId = -1;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
        return invoiceId;
    }
    
    /**
     * Helper method to update the product stock.
     * Deducts the purchased quantity from the product's current stock.
     */
    private static void updateProductStock(int productId, int quantityPurchased, Connection conn) throws SQLException {
        String selectSQL = "SELECT stock FROM Product WHERE product_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int currentStock = rs.getInt("stock");
                    int newStock = currentStock - quantityPurchased;
                    if (newStock < 0) {
                        throw new SQLException("Insufficient stock for product ID: " + productId);
                    }
                    String updateSQL = "UPDATE Product SET stock = ? WHERE product_id = ?";
                    try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateSQL)) {
                        pstmtUpdate.setInt(1, newStock);
                        pstmtUpdate.setInt(2, productId);
                        pstmtUpdate.executeUpdate();
                    }
                } else {
                    throw new SQLException("Product with ID " + productId + " does not exist.");
                }
            }
        }
    }
    
    // You can add more methods later (like getInvoiceById, getAllInvoices, etc.)
    
    // Testing the createInvoice method:
    public static void main(String[] args) {
        // Example usage: Create an invoice for customer_id 1 with two items.
        InvoiceItem item1 = new InvoiceItem(1, 2, 29.99);  // Product 1, quantity 2, price 29.99 each.
        InvoiceItem item2 = new InvoiceItem(2, 1, 49.99);  // Product 2, quantity 1, price 49.99 each.
        List<InvoiceItem> items = List.of(item1, item2);
        int newInvoiceId = createInvoice(1, items);
        if(newInvoiceId != -1) {
            System.out.println("Invoice created with ID: " + newInvoiceId);
        } else {
            System.out.println("Invoice creation failed.");
        }
    }
}


