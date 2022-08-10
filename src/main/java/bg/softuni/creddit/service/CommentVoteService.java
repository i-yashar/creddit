package bg.softuni.creddit.service;

import bg.softuni.creddit.model.entity.Comment;
import bg.softuni.creddit.model.entity.CommentVote;
import bg.softuni.creddit.model.entity.User;
import bg.softuni.creddit.repository.CommentVoteRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CommentVoteService {
    private final CommentVoteRepository commentVoteRepository;

    public CommentVoteService(CommentVoteRepository commentVoteRepository) {
        this.commentVoteRepository = commentVoteRepository;
    }


    public CommentVote findCommentVoteByUserAndComment(User user, Comment comment) {
        CommentVote commentVote = this.commentVoteRepository.findCommentVoteByUsernameAndCommentId(user.getUsername(),
                comment.getId());

        return commentVote != null
                ? commentVote
                : this.createNewCommentVote(user, comment);
    }

    public void updateCommentVote(CommentVote commentVote) {
        this.commentVoteRepository.save(commentVote);
    }

    private CommentVote createNewCommentVote(User user, Comment comment) {
        CommentVote commentVote = new CommentVote(user, comment, 0);

        return this.commentVoteRepository.save(commentVote);
    }

    public void deleteAllCommentVotesOnComment(Long commentId) {
        this.commentVoteRepository.findAll().stream()
                .filter(cv -> Objects.equals(cv.getComment().getId(), commentId))
                .forEach(this.commentVoteRepository::delete);
    }
}
