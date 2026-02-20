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

    }

}
