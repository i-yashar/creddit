package bg.softuni.creddit.model.view;

public class PostDisplayView {
    private long id;
    private String title;
    private String community;
    private String owner;
    private String description;
    private int upvoteCount;
    private int upvoteStatus = 0;

    public PostDisplayView() {
    }

    public int getUpvoteStatus() {
        return upvoteStatus;
    }

    public void setUpvoteStatus(int upvoteStatus) {
        this.upvoteStatus = upvoteStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(int upvoteCount) {
        this.upvoteCount = upvoteCount;
    }
}
