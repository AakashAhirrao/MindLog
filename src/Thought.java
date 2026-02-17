import java.time.LocalDateTime;

public class Thought {

    private int id;
    private Category category;
    private String content;
    private LocalDateTime date;


    public Thought(Category category, String content){
        this.category = category;
        this.content = content;
        this.date = LocalDateTime.now();
    }

    public Thought(int id, Category category, String content, LocalDateTime date){
        this.id = id;
        this.category = category;
        this.content = content;
        this.date = date;
    }

    public int getId() {return id;}

    public Category getCategory() {return category;}

    public String getContent() {return content;}

    public LocalDateTime getDate() {return date;}
}
