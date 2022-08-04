package bg.softuni.creddit.web.rest;

import bg.softuni.creddit.model.dto.PostVoteDTO;
import bg.softuni.creddit.service.PostService;
import bg.softuni.creddit.service.UserService;
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

    public PostRestController(PostService postService) {
        this.postService = postService;
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
}
