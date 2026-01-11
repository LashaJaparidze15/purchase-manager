import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private static final String INSERT_SQL = "INSERT INTO Product (product_name, price, stock, category_id) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = """
        SELECT p.product_id, p.product_name, p.price, p.stock, 
               c.category_id, c.category_name 
        FROM Product p 
        LEFT JOIN Category c ON p.category_id = c.category_id 
        ORDER BY p.product_name""";
    private static final String SELECT_BY_ID_SQL = """
        SELECT p.product_id, p.product_name, p.price, p.stock, 
               c.category_id, c.category_name 
        FROM Product p 
        LEFT JOIN Category c ON p.category_id = c.category_id 
        WHERE p.product_id = ?""";
    private static final String UPDATE_SQL = "UPDATE Product SET product_name = ?, price = ?, stock = ?, category_id = ? WHERE product_id = ?";
    private static final String DELETE_SQL = "DELETE FROM Product WHERE product_id = ?";
    private static final String SEARCH_SQL = """
        SELECT p.product_id, p.product_name, p.price, p.stock, 
               c.category_id, c.category_name 
        FROM Product p 
        LEFT JOIN Category c ON p.category_id = c.category_id 
        WHERE p.product_name LIKE ? 
        ORDER BY p.product_name""";

    public static boolean insertProduct(String name, double price, int stock, int categoryId) {
        if (name == null || name.trim().isEmpty() || price <= 0 || stock < 0 || categoryId <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, name.trim());
            pstmt.setDouble(2, price);
            pstmt.setInt(3, stock);
            pstmt.setInt(4, categoryId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting product: " + e.getMessage());
            return false;
        }
    }

    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getInt("category_id"),
                    rs.getString("category_name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching products: " + e.getMessage());
        }
        return products;
    }

    public static Product getProductById(int id) {
        if (id <= 0) return null;
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    rs.getInt("category_id"),
                    rs.getString("category_name")
                ) : null;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching product: " + e.getMessage());
            return null;
        }
    }

    public static boolean updateProduct(Product product) {
        if (product == null || product.getId() <= 0 || product.getPrice() <= 0 || 
            product.getStock() < 0 || product.getCategoryId() <= 0) {
            return false;
        }

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
            
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getStock());
            pstmt.setInt(4, product.getCategoryId());
            pstmt.setInt(5, product.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteProduct(int id) {
        if (id <= 0) return false;
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return false;
        }
    }

    public static List<Product> searchProducts(String searchTerm) {
        List<Product> products = new ArrayList<>();
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return products;
        }

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SEARCH_SQL)) {
            
            pstmt.setString(1, "%" + searchTerm.trim() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getInt("category_id"),
                        rs.getString("category_name")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching products: " + e.getMessage());
        }
        return products;
    }

    public static class Product {
        private final int id;
        private final String name;
        private final double price;
        private final int stock;
        private final int categoryId;
        private final String categoryName;

        public Product(int id, String name, double price, int stock, int categoryId, String categoryName) {
            this.id = id;
            this.name = name != null ? name : "";
            this.price = price;
            this.stock = stock;
            this.categoryId = categoryId;
            this.categoryName = categoryName != null ? categoryName : "";
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public double getPrice() { return price; }
        public int getStock() { return stock; }
        public int getCategoryId() { return categoryId; }
        public String getCategoryName() { return categoryName; }
    }
}


