import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;
import javax.swing.JOptionPane;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/purchase_manager";
    private static final String USER = "root";
    private static final String ENCRYPTED_PASSWORD = "SjI0blZlQWNYIQ==";
    private static Connection connection = null;
    
    public static Connection connect() {
        try {
            if (connection == null || connection.isClosed()) {
                String password = new String(Base64.getDecoder().decode(ENCRYPTED_PASSWORD));
                connection = DriverManager.getConnection(URL, USER, password);
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("Database Connection Failed: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Database Connection Failed:\n" + e.getMessage(), 
                "Connection Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    public static void testConnection() {
        try (Connection conn = connect()) {
            if (conn != null && conn.isValid(2)) {
                JOptionPane.showMessageDialog(null, 
                    "✅ Database connection successful!", 
                    "Connection Test", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "❌ Connection failed: " + e.getMessage(), 
                "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
