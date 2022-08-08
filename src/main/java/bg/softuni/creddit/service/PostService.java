package bg.softuni.creddit.service;

import bg.softuni.creddit.model.dto.AddCommentDTO;
import bg.softuni.creddit.model.dto.AddPostDTO;
import bg.softuni.creddit.model.dto.PostVoteDTO;
import bg.softuni.creddit.model.entity.*;
import bg.softuni.creddit.model.view.CommentDisplayView;
import bg.softuni.creddit.model.view.PostDisplayView;
import bg.softuni.creddit.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final CommentService commentService;
    private final UserService userService;
    private final VoteService voteService;
    private final CommentVoteService commentVoteService;
    private final PostRepository postRepository;
    private final CommunityService communityService;
    private final ModelMapper modelMapper;

    public PostService(CommentService commentService, UserService userService, VoteService voteService, CommentVoteService commentVoteService, PostRepository postRepository, CommunityService communityService, ModelMapper modelMapper) {
        this.commentService = commentService;
        this.userService = userService;
        this.voteService = voteService;
        this.commentVoteService = commentVoteService;
        this.postRepository = postRepository;
        this.communityService = communityService;
        this.modelMapper = modelMapper;
    }

    public Page<PostDisplayView> retrieveAllPostsPaginationEnabled(Pageable pageable) {
        Page<Post> allPosts = this.postRepository.findAll(pageable);

        return allPosts
                .map(p -> modelMapper.map(p, PostDisplayView.class))
                .map(p -> {
                    User user = this.userService.getCurrentUser();
                    if(user != null) {
                        Vote vote = this.voteService.findVoteByUserAndPost(
                                user,
                                this.getPostById(p.getId())
                        );
                        p.setUpvoteStatus(vote.getValue());
                    }
                    return p;
                });
    }

    public List<PostDisplayView> retrieveAllPosts() {
        List<Post> allPosts = this.postRepository.findAll();

        return allPosts
                .stream()
                .map(p -> modelMapper.map(p, PostDisplayView.class))
                .map(p -> {
                    User user = this.userService.getCurrentUser();
                    if(user != null) {
                        Vote vote = this.voteService.findVoteByUserAndPost(
                                user,
                                this.getPostById(p.getId())
                        );
                        p.setUpvoteStatus(vote.getValue());
                    }
                    return p;
                })
                .collect(Collectors.toList());
    }

    public PostDisplayView retrievePostById(Long postId) {
        Post post = this.getPostById(postId);

        PostDisplayView postDisplayView = this.modelMapper.map(post, PostDisplayView.class);

        User user = this.userService.getCurrentUser();

        if(user != null) {
            Vote vote = this.voteService.findVoteByUserAndPost(
                    user,
                    this.getPostById(postDisplayView.getId())
            );
            postDisplayView.setUpvoteStatus(vote.getValue());
        }

        return postDisplayView;
    }

    public List<CommentDisplayView> loadPostComments(Long postId) {
        return this.commentService.findAllCommentsOnPost(postId)
                .stream()
                .map(c -> {
                    User user = this.userService.getCurrentUser();
                    if(user != null) {
                        CommentVote commentVote = this.commentVoteService.findCommentVoteByUserAndComment(
                                user,
                                this.commentService.getCommentById(c.getId())
                        );
                        c.setUpvoteStatus(commentVote.getValue());
                    }
                    return c;
                })
                .collect(Collectors.toList());
    }

    public void addPost(AddPostDTO addPostDTO, String username) {
        Post post = modelMapper.map(addPostDTO, Post.class);
        post.setUpvoteCount(0);
        post.setComments(new ArrayList<>());
        post.setCreatedOn(LocalDateTime.now());
        post.setOwner(this.userService.getUserByUsername(username));

        Community community = this.communityService.getCommunityByName(addPostDTO.getCommunity());

        post.setCommunity(community);

        this.postRepository.save(post);
    }

    @Transactional
    public PostVoteDTO upVotePost(String username, Long postId) {

        Vote vote = this.voteService.findVoteByUserAndPost(this.userService.getUserByUsername(username), getPostById(postId));
        Post post = this.getPostById(postId);

        if (vote.getValue() == 1) {
            vote.setValue(0);
            post.setUpvoteCount(post.getUpvoteCount() - 1);
        } else if (vote.getValue() == -1) {
            vote.setValue(1);
            post.setUpvoteCount(post.getUpvoteCount() + 2);
        } else if (vote.getValue() == 0) {
            vote.setValue(1);
            post.setUpvoteCount(post.getUpvoteCount() + 1);
        }

        this.voteService.updateVote(vote);
        this.postRepository.save(post);

        PostVoteDTO postVoteDTO = new PostVoteDTO();
        postVoteDTO.setUpVoteCount(this.postRepository.findById(postId).get().getUpvoteCount());

        return postVoteDTO;
    }
    @Transactional
    public PostVoteDTO downVotePost(String username, Long postId) {

        Vote vote = this.voteService.findVoteByUserAndPost(this.userService.getUserByUsername(username), getPostById(postId));
        Post post = this.getPostById(postId);

        if (vote.getValue() == -1) {
            vote.setValue(0);
            post.setUpvoteCount(post.getUpvoteCount() + 1);
        } else if (vote.getValue() == 1) {
            vote.setValue(-1);
            post.setUpvoteCount(post.getUpvoteCount() - 2);
        } else if (vote.getValue() == 0) {
            vote.setValue(-1);
            post.setUpvoteCount(post.getUpvoteCount() - 1);
        }

        this.voteService.updateVote(vote);
        this.postRepository.save(post);

        PostVoteDTO postVoteDTO = new PostVoteDTO();
        postVoteDTO.setUpVoteCount(this.postRepository.findById(postId).get().getUpvoteCount());

        return postVoteDTO;
    }

    public void addComment(AddCommentDTO addCommentDTO, Long postId, String username) {

        Post post = this.getPostById(postId);
        User user = this.userService.getUserByUsername(username);

        this.commentService.addComment(addCommentDTO.getComment(), post, user);
    }

    protected Post getPostById(Long postId) {
        return this.postRepository
                .findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    public List<PostDisplayView> getAllPostsByUsername(String username) {
        return this.postRepository
                .findAllByOwnerUsername(username)
                .stream()
                .map(p -> modelMapper.map(p,PostDisplayView.class))
                .collect(Collectors.toList());
    }
}
