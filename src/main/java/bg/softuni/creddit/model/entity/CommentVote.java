package bg.softuni.creddit.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "comment_votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "comment_id"})})
public class CommentVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Comment comment;

    @Column(nullable = false)
    private Integer value;

    public CommentVote() {
    }

    public CommentVote(User user, Comment comment, Integer value) {
        this.user = user;
        this.comment = comment;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
