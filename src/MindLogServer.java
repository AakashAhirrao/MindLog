import io.javalin.Javalin;
import java.util.List;

public class MindLogServer {

    public static JournalService journal = new SqlJournalService();

    public static void main(String[] args){

        var app = Javalin.create().start(7070);

//        System.out.println("🚀 MindLog Web Server is running on http://localhost:7070");

        app.get("/thoughts", ctx -> {
           List<Thought> thoughts = journal.getAllEntries();
           ctx.json(thoughts);
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

}
