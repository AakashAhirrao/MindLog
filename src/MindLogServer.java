import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;

public class MindLogServer {

    // Using your existing service
    public static JournalService journal = new SqlJournalService();

    public static void main(String[] args) {
        // 1. Setup the JSON tool to handle Java Dates (LocalDateTime)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // 2. Tell Javalin to use this "Date-Smart" mapper by default
        JavalinJackson.defaultMapper();
        // 3. Start the serve
        var app = Javalin.create(config -> {
            config.staticFiles.add("/public");
        }).start(7070);

        System.out.println("🚀 MindLog Web Server is running on http://localhost:7070");

        // --- READ: Get all thoughts ---
        app.get("/thoughts", ctx -> {

            String keyword = ctx.queryParam("keyword");

            if (keyword != null && !keyword.isEmpty()) {
                ctx.json(journal.searchEntries(keyword));
            } else {
                ctx.json(journal.getAllEntries());
            }
//            List<Thought> thoughts = journal.getAllEntries();
//            ctx.json(thoughts);
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