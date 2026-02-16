import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlJournalService implements JournalService{

    @Override
    public void addEntry(Thought thought){
        String sql = "INSERT INTO thoughts (category, content) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, thought.getCategory());
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
                             rs.getString("category"),
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

    @Override public List<Thought> getEntriesByCategory(String c) { return new ArrayList<>();}
    @Override public List<Thought> searchEntries(String k) {return new ArrayList<>();}
    @Override public void updateEntry(int id, String text) { }
    @Override public void deleteEntry(int id) { }
}
