import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Thought {

    private int id;
    private int userId;
    private Category category;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;


    public Thought() {
    }

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

    public int getUserId() {return userId;};

    public void setUserId(int userId) {this.userId = userId;};
}
