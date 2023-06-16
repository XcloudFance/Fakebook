package dsgp6.fakebook.model;


public class PostResponse {
    private Post post;
    private String fromPostContent;

    public PostResponse() {
    }

    public PostResponse(Post post, String fromPostContent) {
        this.post = post;
        this.fromPostContent = fromPostContent;
    }

    // Getters and setters
}

