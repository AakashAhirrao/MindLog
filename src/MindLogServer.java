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

        app.post("/thoughts", ctx -> {
            Thought newThought = ctx.bodyAsClass(Thought.class);
            journal.addEntry(newThought);
            ctx.status(201).result("Thought saved successfully!");
        });

        app.delete("/thoughts/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            if (journal.existsId(id)) {
                journal.deleteEntry(id);
                ctx.result("Deleted thought #" + id);
            } else {
                ctx.status(404).result("Thought not found");
            }
        });

    }

}
