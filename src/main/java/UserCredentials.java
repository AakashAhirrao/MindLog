// We use this class JUST to catch the JSON coming from the login screen!
public class UserCredentials {
    private String username;
    private String password;

    public UserCredentials() {
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}