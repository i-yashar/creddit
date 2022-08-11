package bg.softuni.creddit.service;

import bg.softuni.creddit.exception.notfound.CommentNotFoundException;
import bg.softuni.creddit.model.dto.CommentVoteDTO;
import bg.softuni.creddit.model.entity.*;
import bg.softuni.creddit.model.view.CommentDisplayView;
import bg.softuni.creddit.repository.CommentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentVoteService commentVoteService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final static int UP_VOTE = 1;
    private final static int DOWN_VOTE = -1;

    public CommentService(CommentRepository commentRepository, CommentVoteService commentVoteService, UserService userService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.commentVoteService = commentVoteService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public void addComment(String content, Post post, User owner) {
        Comment comment = new Comment(content, 0, post, owner);
        this.commentRepository.save(comment);
    }

    public List<CommentDisplayView> findAllCommentsOnPost(Long postId) {
        return this.commentRepository.findAllByPostId(postId)
                .stream()
                .map(c -> modelMapper.map(c, CommentDisplayView.class))
                .collect(Collectors.toList());
    }

    public int findCommentCountOnPost(Long postId) {
        return this.findAllCommentsOnPost(postId).size();
    }

    public boolean isOwner(String username, Long commentId) {
        return this.getCommentById(commentId).getOwner().getUsername().equals(username);
    }

    @Transactional
    public CommentVoteDTO upVoteComment(String username, Long commentId) {
        Comment comment = this.getCommentById(commentId);

        CommentVote commentVote = this.commentVoteService.findCommentVoteByUserAndComment(
                this.userService.getUserByUsername(username),
                comment
        );

        return this.giveCommentVote(comment, commentVote, UP_VOTE);
    }

    @Transactional
    public CommentVoteDTO downVoteComment(String username, Long commentId) {
        Comment comment = this.getCommentById(commentId);

        CommentVote commentVote = this.commentVoteService.findCommentVoteByUserAndComment(
                this.userService.getUserByUsername(username),
                comment
        );

        return this.giveCommentVote(comment, commentVote, DOWN_VOTE);
    }

    @Transactional
    public void distributeCredits(String username, Long commentId) {
        User user = this.getCommentById(commentId).getOwner();
        String commentOwner = user.getUsername();

        if(commentOwner.equals(username)) {
            return;
        }

        user.setCredits(0);

        int creditSum = this.commentRepository.findAll().stream()
                .filter(c -> c.getOwner().getUsername().equals(commentOwner))
                .map(Comment::getUpvoteCount)
                .mapToInt(Integer::intValue)
                .sum();

        user.setCredits(Math.max(0, creditSum));
    }

    protected Comment getCommentById(Long commentId) {
        return this.commentRepository
                .findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId +
                        " not found."));
    }

    private CommentVoteDTO giveCommentVote(Comment comment, CommentVote commentVote, int vote) {
        switch (commentVote.getValue()) {
            case 1 -> {
                commentVote.setValue(vote == UP_VOTE ? 0 : -1);
                comment.setUpvoteCount(comment.getUpvoteCount() - (vote == UP_VOTE ? 1 : 2));
            }
            case -1 -> {
                commentVote.setValue(vote == UP_VOTE ? 1 : 0);
                comment.setUpvoteCount(comment.getUpvoteCount() + (vote == UP_VOTE ? 2 : 1));
            }
            case 0 -> {
                commentVote.setValue(vote == UP_VOTE ? 1 : -1);
                comment.setUpvoteCount(comment.getUpvoteCount() + (vote == UP_VOTE ? 1 : -1));
            }
        }

        this.commentVoteService.updateCommentVote(commentVote);
        this.commentRepository.save(comment);

        CommentVoteDTO commentVoteDTO = new CommentVoteDTO();
        commentVoteDTO.setUpVoteCount(comment.getUpvoteCount());

        return commentVoteDTO;
    }

    public void deleteAllCommentsOnPost(Long postId) {
        this.commentRepository.findAll().stream()
                .filter(c -> Objects.equals(c.getPost().getId(), postId))
                .forEach(comment -> {
                    this.commentVoteService.deleteAllCommentVotesOnComment(comment.getId());
                    this.commentRepository.delete(comment);
                });
    }

    public void deleteComment(Long commentId) {
        this.commentVoteService.deleteAllCommentVotesOnComment(commentId);

        Comment comment = this.getCommentById(commentId);
        this.commentRepository.delete(comment);
    }
}