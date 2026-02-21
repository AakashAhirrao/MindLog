import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MindLogServer {

    // Using your existing service
    public static JournalService journal = new SqlJournalService();

    public static UserService userService = new UserService();

    public static void main(String[] args) {
        // 1. Set up the JSON tool to handle Java Dates (LocalDateTime)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // 2. Tell Javalin to use this "Date-Smart" mapper by default
        JavalinJackson.defaultMapper();

        String portStr = System.getenv("PORT");
        int port = (portStr != null) ? Integer.parseInt(portStr) : 7070;

        // 3. Start the serve
        var app = Javalin.create(config -> {
            config.staticFiles.add("/public");
        }).start(port);

        System.out.println("🚀 Server started on port: "+port);

        app.post("/register", ctx -> { // REGISTER GATE
            // 1. Catch the JSON envelope
            UserCredentials credentials = ctx.bodyAsClass(UserCredentials.class);

            // 2. Ask our service to register them
            boolean success = userService.registerUser(credentials.getUsername(), credentials.getPassword());

            if (success) {
                ctx.status(201).result("User created successfully!");
            } else {
                // 400 means "Bad Request" (like the username was taken)
                ctx.status(400).result("Username already exists!");
            }
        });

        // --- LOGIN GATE ---
        app.post("/login", ctx -> {
            // 1. Catch the JSON envelope
            UserCredentials credentials = ctx.bodyAsClass(UserCredentials.class);

            // 2. Ask our service to check the password
            User loggedInUser = userService.authenticateUser(credentials.getUsername(), credentials.getPassword());

            if (loggedInUser != null) {
                // Success! Send the user details back to the browser.
                // (Jackson will automatically hide the passwordHash if we don't have a getter for it,
                // but it's safe to send the ID and username!)
                ctx.status(200).json(loggedInUser);
            } else {
                // 401 means "Unauthorized" (Wrong password or username)
                ctx.status(401).result("Invalid username or password");
            }
        });

        // --- READ: Get all thoughts ---
        // --- READ: Get all thoughts ---
        app.get("/thoughts", ctx -> {
            String keyword = ctx.queryParam("keyword");

            // Get the limit and offset (Your existing code)
            String limitStr = ctx.queryParam("limit");
            String offsetStr = ctx.queryParam("offset");
            int limit = (limitStr != null) ? Integer.parseInt(limitStr) : 5;
            int offset = (offsetStr != null) ? Integer.parseInt(offsetStr) : 0;

            // --- NEW STUFF: Grab the User ID from the URL ---
            String userIdStr = ctx.queryParam("userId");

            // Security Check: If they don't have an ID, kick them out!
            if (userIdStr == null) {
                ctx.status(401).result("Unauthorized: Please log in!");
                return; // This stops the code from running any further
            }

            int userId = Integer.parseInt(userIdStr);
            // ------------------------------------------------

            if (keyword != null && !keyword.isEmpty()) {
                ctx.json(journal.searchEntries(keyword));
            } else {
                // Notice we added 'userId' inside the brackets here!
                ctx.json(journal.getPagedEntries(userId, limit, offset));
            }
        });

        // --- CREATE: Add a new thought --
        app.post("/thoughts", ctx -> {
            Thought newThought = ctx.bodyAsClass(Thought.class);
            journal.addEntry(newThought);
            ctx.status(201).result("Thought saved successfully!");
        });

        // --- DELETE: Remove a thought ---
        app.delete("/thoughts/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            if (journal.existsId(id)) { // Using your check method
                journal.deleteEntry(id);
                ctx.result("Deleted thought #" + id);
            } else {
                ctx.status(404).result("Thought not found");
            }
        });

        // READ by Category: e.g., /thoughts/category/fitness
        app.get("/thoughts/category/{name}", ctx -> {
            try {
                // Convert the URL text (like "work") into our Enum (WORK)
                Category cat = Category.valueOf(ctx.pathParam("name").toUpperCase());
                ctx.json(journal.getEntriesByCategory(cat));
            } catch (IllegalArgumentException e) {
                ctx.status(400).result("Invalid category! Use: WORK, PERSONAL, IDEA, or FITNESS");
            }
        });

        // UPDATE: Change an existing thought
        app.put("/thoughts/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));

            if (journal.existsId(id)) {
                // Grab the new content from the request body
                Thought updatedData = ctx.bodyAsClass(Thought.class);

                journal.updateEntry(id, updatedData.getContent());
                ctx.result("Thought #" + id + " updated successfully!");
            } else {
                ctx.status(404).result("Cannot update: Thought not found.");
            }
        });
    }
}