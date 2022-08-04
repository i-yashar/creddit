package bg.softuni.creddit.web.rest;

import bg.softuni.creddit.model.dto.CommentVoteDTO;
import bg.softuni.creddit.model.dto.PostVoteDTO;
import bg.softuni.creddit.service.CommentService;
import bg.softuni.creddit.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {
    private final PostService postService;
    private final CommentService commentService;

    public PostRestController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/upvote/{postId}")
    public ResponseEntity<PostVoteDTO> upVote(@PathVariable(name = "postId") Long postId,
                                              Principal principal) {
        PostVoteDTO postVoteDTO = this.postService.upVotePost(principal.getName(), postId);
        return ResponseEntity.ok(postVoteDTO);
    }

    @GetMapping("/downvote/{postId}")
    public ResponseEntity<PostVoteDTO> downVote(@PathVariable(name = "postId") Long postId,
                                              Principal principal) {
        PostVoteDTO postVoteDTO = this.postService.downVotePost(principal.getName(), postId);
        return ResponseEntity.ok(postVoteDTO);
    }

    @GetMapping("/comments/upvote/{commentId}")
    public ResponseEntity<CommentVoteDTO> upVoteComment(@PathVariable(name = "commentId") Long commentId,
                                                        Principal principal) {
        CommentVoteDTO commentVoteDTO = this.commentService.upVoteComment(principal.getName(), commentId);
        return ResponseEntity.ok(commentVoteDTO);
    }

    @GetMapping("/comments/downvote/{commentId}")
    public ResponseEntity<CommentVoteDTO> downVoteComment(@PathVariable(name = "commentId") Long commentId,
                                                        Principal principal) {
        CommentVoteDTO commentVoteDTO = this.commentService.downVoteComment(principal.getName(), commentId);
        return ResponseEntity.ok(commentVoteDTO);
    }
}
