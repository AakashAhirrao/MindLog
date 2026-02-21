import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlJournalService implements JournalService{

    @Override
    public void addEntry(Thought thought){
        String sql = "INSERT INTO thoughts (category, content) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, thought.getCategory().name());
            stmt.setString(2, thought.getContent());

            stmt.executeUpdate();
            System.out.println("Thought saved to MySQL! ");
        }
        catch (SQLException e){
            System.err.println("Error saving thought: " + e.getMessage());
        }
    }

    @Override
    public List<Thought> getAllEntries() {
        List<Thought> list = new ArrayList<>();
        String sql = "SELECT * FROM thoughts ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

                 while(rs.next()) {
                     // Returning each row from database to Thought object
                     Thought t = new Thought(
                             rs.getInt("id"),
                             Category.valueOf(rs.getString("category").toUpperCase()),
                             rs.getString("content"),
                             rs.getTimestamp("created_at").toLocalDateTime()
                     );
                     list.add(t);
                 }
        }
        catch (SQLException e) {
            System.err.println("Error reading thoughts: " + e.getMessage());
        }
        return list;
    }

    @Override public List<Thought> getEntriesByCategory(Category category) {

        List<Thought> results = new ArrayList<>();

        String sql = "SELECT * FROM thoughts WHERE category = ? ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.name());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                results.add(new Thought(
                        rs.getInt("id"),
                        Category.valueOf(rs.getString("category").toUpperCase()),
                        rs.getString("content"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e){
            System.out.println("Filtering error: "+e.getMessage());
        }
        return results;
    }

    @Override public List<Thought> searchEntries(String keyword) {
        List<Thought> result = new ArrayList<>();

        String sql = "SELECT * FROM thoughts WHERE content LIKE ? OR category LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String query = "%"+keyword+"%";
            stmt.setString(1, query);
            stmt.setString(2, query);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(new Thought(
                        rs.getInt("id"),
                        Category.valueOf(rs.getString("category").toUpperCase()),
                        rs.getString("content"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e){
            System.out.println("Search error: "+e.getMessage());
        }

        return result;
    }
    @Override public void updateEntry(int id, String newContent) {

        String sql = "UPDATE thoughts SET content = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newContent);
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0){
                System.out.println("Thought #" +id+ " updated successfully!");
            }
            else {
                System.out.println("No thought found with ID: "+id);
            }
        }
        catch (SQLException e){
            System.out.println("Update Error: "+e.getMessage());
        }
    }
    @Override public void deleteEntry(int id) {

        String sql = "DELETE FROM thoughts WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("Error in deletion: "+e.getMessage());
        }
    }

    @Override public boolean existsId(int id){

        String sql = "SELECT COUNT(*) FROM thoughts WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        catch (SQLException e){
            System.out.println("Database check error: "+e.getMessage());
        }
        return false;
    }

    @Override
    public List<Thought> getPagedEntries(int limit, int offset) {
        List<Thought> results = new ArrayList<>();
        // LIMIT = amount to fetch | OFFSET = amount to skip
        String sql = "SELECT * FROM thoughts ORDER BY created_at DESC LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(new Thought(
                        rs.getInt("id"),
                        Category.valueOf(rs.getString("category").toUpperCase()),
                        rs.getString("content"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Pagination error: " + e.getMessage());
        }
        return results;
    }
}
