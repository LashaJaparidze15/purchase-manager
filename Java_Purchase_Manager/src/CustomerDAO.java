import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private static final String INSERT_SQL = "INSERT INTO Customer (name, email, phone) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM Customer ORDER BY name";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM Customer WHERE customer_id = ?";
    private static final String UPDATE_SQL = "UPDATE Customer SET name = ?, email = ?, phone = ? WHERE customer_id = ?";
    private static final String DELETE_SQL = "DELETE FROM Customer WHERE customer_id = ?";
    private static final String SEARCH_SQL = "SELECT * FROM Customer WHERE name LIKE ? ORDER BY name";

    public static boolean insertCustomer(String name, String email, String phone) {
        if (name == null || name.trim().isEmpty() || 
            email == null || email.trim().isEmpty() || 
            phone == null || phone.trim().isEmpty()) {
            return false;
        }

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, name.trim());
            pstmt.setString(2, email.trim());
            pstmt.setString(3, phone.trim());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting customer: " + e.getMessage());
            return false;
        }
    }

    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching customers: " + e.getMessage());
        }
        return customers;
    }

    public static Customer getCustomerById(int id) {
        if (id <= 0) return null;
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getTimestamp("created_at")
                ) : null;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching customer: " + e.getMessage());
            return null;
        }
    }

    public static boolean updateCustomer(Customer customer) {
        if (customer == null || customer.getId() <= 0) return false;
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.setInt(4, customer.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteCustomer(int id) {
        if (id <= 0) return false;
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }

    public static List<Customer> searchCustomers(String searchTerm) {
        List<Customer> customers = new ArrayList<>();
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return customers;
        }

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SEARCH_SQL)) {
            
            pstmt.setString(1, "%" + searchTerm.trim() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching customers: " + e.getMessage());
        }
        return customers;
    }

    public static class Customer {
        private final int id;
        private final String name;
        private final String email;
        private final String phone;
        private final Timestamp createdAt;

        public Customer(int id, String name, String email, String phone, Timestamp createdAt) {
            this.id = id;
            this.name = name != null ? name : "";
            this.email = email != null ? email : "";
            this.phone = phone != null ? phone : "";
            this.createdAt = createdAt;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public Timestamp getCreatedAt() { return createdAt; }
    }
}