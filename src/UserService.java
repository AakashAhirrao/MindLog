import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    public boolean registerUser(String username, String rawPassword) {
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        String sql = "INSERT INTO users(username, password_hash) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();

            System.out.println("New user registered!");
            return true;
        } catch (SQLException e){
            System.out.println("Username already taken!!!");
            return false;
        }
    }

    public User authenticateUser (String username, String rawPassword){
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){ // next returns true if query executed perfectly, false otherwise
                String storedHash = rs. getString("password_hash");

                if(BCrypt.checkpw(rawPassword, storedHash)){
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            storedHash
                    );
                }
            }
        } catch (SQLException e){
            System.out.println("Login error: "+e.getMessage());
        }

        return null;
    }
}
