import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private static final String INSERT_SQL = "INSERT INTO Category (category_name) VALUES (?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM Category ORDER BY category_name";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM Category WHERE category_id = ?";
    private static final String UPDATE_SQL = "UPDATE Category SET category_name = ? WHERE category_id = ?";
    private static final String DELETE_SQL = "DELETE FROM Category WHERE category_id = ?";
    private static final String SEARCH_SQL = "SELECT * FROM Category WHERE category_name LIKE ? ORDER BY category_name";

    public static boolean insertCategory(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty())
            return false;
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, categoryName.trim());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting category: " + e.getMessage());
            return false;
        }
    }

    public static List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching categories: " + e.getMessage());
        }
        return categories;
    }

    public static Category getCategoryById(int id) {
        if (id <= 0) return null;
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(rs.getInt("category_id"), rs.getString("category_name"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching category: " + e.getMessage());
        }
        return null;
    }

    public static boolean updateCategory(Category category) {
        if (category == null || category.getId() <= 0)
            return false;
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            pstmt.setString(1, category.getName());
            pstmt.setInt(2, category.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating category: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteCategory(int id) {
        if (id <= 0) return false;
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting category: " + e.getMessage());
            return false;
        }
    }

    public static List<Category> searchCategories(String searchTerm) {
        List<Category> categories = new ArrayList<>();
        if (searchTerm == null || searchTerm.trim().isEmpty()) return categories;
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SEARCH_SQL)) {
            pstmt.setString(1, "%" + searchTerm.trim() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    categories.add(new Category(rs.getInt("category_id"), rs.getString("category_name")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching categories: " + e.getMessage());
        }
        return categories;
    }

    public static class Category {
        private final int id;
        private final String name;

        public Category(int id, String name) {
            this.id = id;
            this.name = name;
        }
        public int getId() { return id; }
        public String getName() { return name; }
        @Override
        public String toString() {
            return name; // Useful for displaying in combo boxes, etc.
        }
    }
}