package dsgp6.fakebook.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    String uid;
    private String content;
    private int likes;
    private int views;
    private int forwards;
    private ArrayList<Comment> comments = new ArrayList<>();

    private LocalDateTime postTime;

    public Post() {
    }

    public void forward() {
        forwards ++;
    }

    public void deleteForward() {
        forwards --;
    }

    public int getForwards() {
        return forwards;
    }

    public Post(String content, String uid) {
        this.content = content;
        this.uid = uid;
        this.postTime = LocalDateTime.now();
    }

    public String getUid() {
        return uid;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public int getCommentsNumber()
    {
        return comments.size();
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void incrementLikes() {
        this.likes++;
    }

    public void incrementViews() {
        this.views++;
    }

    public void increaseForwards() {
        forwards ++;
    }
}
