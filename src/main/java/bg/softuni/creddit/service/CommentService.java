package bg.softuni.creddit.service;

import bg.softuni.creddit.model.dto.PostVoteDTO;
import bg.softuni.creddit.model.entity.*;
import bg.softuni.creddit.model.view.CommentDisplayView;
import bg.softuni.creddit.repository.CommentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentVoteService commentVoteService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository, CommentVoteService commentVoteService, UserService userService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.commentVoteService = commentVoteService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public void addComment(String content, Post post, User user) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setOwner(user);
        comment.setUpvoteCount(0);
        this.commentRepository.save(comment);
    }

    public List<CommentDisplayView> findAllCommentsOnPost(Long postId) {
        return this.commentRepository.findAllByPostId(postId)
                .stream()
                .map(c -> modelMapper.map(c, CommentDisplayView.class))
                .collect(Collectors.toList());
    }

//    public void upvoteComment(Long commentId) {
//
//        Comment comment = this.getCommentById(commentId);
//
//        User owner = comment.getOwner();
//
//        comment.setUpvoteCount(comment.getUpvoteCount() + 1);
//
//        owner.setCredits(owner.getCredits() + comment.getUpvoteCount() / 5);
//    }
//
//    @Transactional
//    public PostVoteDTO upvoteComment(String username, Long commentId) {
//
//        CommentVote commentVote = this.commentVoteService.findCommentVoteByUserAndComment(
//                this.userService.getUserByUsername(username),
//                this.getCommentById(commentId)
//        );
//
//        Comment comment = this.getCommentById(commentId);
//
//        if (commentVote.getValue() == 1) {
//            commentVote.setValue(0);
//            comment.setUpvoteCount(comment.getUpvoteCount() - 1);
//        } else if (commentVote.getValue() == -1) {
//            commentVote.setValue(1);
//            comment.setUpvoteCount(comment.getUpvoteCount() + 2);
//        } else if (commentVote.getValue() == 0) {
//            commentVote.setValue(1);
//            comment.setUpvoteCount(comment.getUpvoteCount() + 1);
//        }
//
//        this.commentVoteService.updateCommentVote(commentVote);
//        this.commentRepository.save(comment);
//
//        PostVoteDTO postVoteDTO = new PostVoteDTO();
//        postVoteDTO.setUpVoteCount(this.postRepository.findById(postId).get().getUpvoteCount());
//
//        return postVoteDTO;
//    }
//    @Transactional
//    public PostVoteDTO downVotePost(String username, Long postId) {
//
//        Vote vote = this.voteService.findVoteByUserAndPost(this.userService.getUserByUsername(username), getPostById(postId));
//        Post post = this.getPostById(postId);
//
//        if (vote.getValue() == -1) {
//            vote.setValue(0);
//            post.setUpvoteCount(post.getUpvoteCount() + 1);
//        } else if (vote.getValue() == 1) {
//            vote.setValue(-1);
//            post.setUpvoteCount(post.getUpvoteCount() - 2);
//        } else if (vote.getValue() == 0) {
//            vote.setValue(-1);
//            post.setUpvoteCount(post.getUpvoteCount() - 1);
//        }
//
//        this.voteService.updateVote(vote);
//        this.postRepository.save(post);
//
//        PostVoteDTO postVoteDTO = new PostVoteDTO();
//        postVoteDTO.setUpVoteCount(this.postRepository.findById(postId).get().getUpvoteCount());
//
//        return postVoteDTO;
//    }
//
//    protected Comment getCommentById(Long commentId) {
//        return this.commentRepository
//                .findById(commentId)
//                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
//    }
}