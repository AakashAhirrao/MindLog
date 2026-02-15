import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlJournalService {

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
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
