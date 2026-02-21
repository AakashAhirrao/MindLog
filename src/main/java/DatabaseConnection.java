import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/journal_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Aakash@#23";

    public static Connection getConnection() throws SQLException {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if(url == null){
            url = "jdbc:mysql://localhost:3306/journal_db";
            user = "root";
            password = "yAakash@#23";
        }

        return DriverManager.getConnection(url, user, password);
    }

}
