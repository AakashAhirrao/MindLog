public class User {
    private int id;
    private String username;
    private String passwordHash; // Notice we DO NOT call this "password"!

    // Empty constructor for Jackson (the JSON tool)
    public User() {
    }

    // For registering new user
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // Constructor for pulling an existing user from the database
    public User(int id, String username, String passwordHash) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
}
