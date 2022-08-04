package bg.softuni.creddit.web;

import bg.softuni.creddit.model.dto.AddCommentDTO;
import bg.softuni.creddit.model.dto.AddPostDTO;
import bg.softuni.creddit.service.CommentService;
import bg.softuni.creddit.service.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @ModelAttribute("addCommentDTO")
    public AddCommentDTO initAddCommentDTO() {
        return new AddCommentDTO();
    }

    @ModelAttribute("addPostModel")
    public AddPostDTO initAddPostDTO() {
        return new AddPostDTO();
    }

    @GetMapping("/comments/{postId}")
    public String showComments(@PathVariable(name = "postId") Long postId, Model model) {
        model.addAttribute("post", this.postService.retrievePostById(postId));
        model.addAttribute("comments", this.postService.loadPostComments(postId));

        return "post-comments";
    }

    @GetMapping("/comments/upvote/{postId}/{commentId}")
    public String upvoteComment(@PathVariable(name = "postId") Long postId,
                                @PathVariable(name = "commentId") Long commentId,
                                Principal principal) {

        this.commentService.upVoteComment(principal.getName(), commentId);

        return "redirect:/posts/comments/" + postId;
    }

    @GetMapping("/upvote/{postId}")
    @PreAuthorize("isAuthenticated()")
    public String upVote(@PathVariable(value = "postId") Long postId,
                         Principal principal) {
        this.postService.upVotePost(principal.getName(), postId);
        return "redirect:/";
    }

    @GetMapping("/downvote/{postId}")
    @PreAuthorize("isAuthenticated()")
    public String downVote(@PathVariable(value = "postId") Long postId,
                           Principal principal) {
        this.postService.downVotePost(principal.getName(), postId);
        return "redirect:/";
    }

    @GetMapping("/addPost")
    public String addPost() {
        return "new-post";
    }

    @PostMapping("/addPost")
    public String addNewPost(@Valid AddPostDTO addPostModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Principal principal) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addPostModel", addPostModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addPostModel", bindingResult);
            return "redirect:/posts/addPost";
        }

        this.postService.addPost(addPostModel, principal.getName());

        return "redirect:/dashboard";
    }

    @GetMapping("/addComment/{postId}")
    public String addComment(@PathVariable(name = "postId") Long postId, Model model) {
        model.addAttribute("post", this.postService.retrievePostById(postId));

        return "add-comment";
    }

    @PostMapping("/addComment/{postId}")
    public String addComment(@Valid AddCommentDTO addCommentDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             @PathVariable(name = "postId") Long postId,
                             Principal principal) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addCommentDTO", addCommentDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addCommentDTO", bindingResult);
            return "redirect:/posts/addComment/addPost/" + postId;
        }

        this.postService.addComment(addCommentDTO, postId, principal.getName());

        return "redirect:/posts/comments/" + postId;
    }
}
