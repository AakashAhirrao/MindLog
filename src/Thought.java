import java.time.LocalDateTime;

public class Thought {

    private int id;
    private String category;
    private String content;
    private LocalDateTime date;


    public Thought(String category, String content){
        this.category = category;
        this.content = content;
        this.date = LocalDateTime.now();
    }

    public Thought(int id, String category, String content, LocalDateTime date){
        this.id = id;
        this.category = category;
        this.content = content;
        this.date = date;
    }

    public int getId() {return id;}

    public String getCategory() {return category;}

    public String getContent() {return content;}

    public LocalDateTime getDate() {return date;}
}
