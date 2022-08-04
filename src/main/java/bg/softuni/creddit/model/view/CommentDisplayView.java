package bg.softuni.creddit.model.view;

public class CommentDisplayView {
    private Long id;
    private String content;
    private int upvoteCount;
    private String owner;
    private int upvoteStatus = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUpvoteStatus() {
        return upvoteStatus;
    }

    public void setUpvoteStatus(int upvoteStatus) {
        this.upvoteStatus = upvoteStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(int upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
