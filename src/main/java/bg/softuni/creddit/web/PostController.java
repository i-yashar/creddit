package bg.softuni.creddit.web;

import bg.softuni.creddit.model.dto.AddCommentDTO;
import bg.softuni.creddit.model.dto.AddPostDTO;
import bg.softuni.creddit.service.CommentService;
import bg.softuni.creddit.service.PostService;
import org.springframework.http.HttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
    }

    @ModelAttribute("addCommentDTO")
    public AddCommentDTO initAddCommentDTO() {
        return new AddCommentDTO();
    }

    @ModelAttribute("addPostModel")
    public AddPostDTO initAddPostDTO() {
        return new AddPostDTO();
    }

    @GetMapping("/{postId}/comments")
    public String showComments(@PathVariable("postId") Long postId, Model model) {
        model.addAttribute("post", this.postService.retrievePostById(postId));
        model.addAttribute("comments", this.postService.loadPostComments(postId));
        model.addAttribute("roles", SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return "post-comments";
    }

    @GetMapping("/addPost")
    public String addPost() {
        return "create-post";
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

    @GetMapping("/{postId}/addComment")
    public String addComment(@PathVariable("postId") Long postId, Model model) {
        model.addAttribute("post", this.postService.retrievePostById(postId));

        return "add-comment";
    }

    @PostMapping("/{postId}/addComment")
    public String addComment(@Valid AddCommentDTO addCommentDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             @PathVariable("postId") Long postId,
                             Principal principal) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addCommentDTO", addCommentDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addCommentDTO", bindingResult);
            return "redirect:/posts/addComment/addPost/" + postId;
        }

        this.postService.addComment(addCommentDTO, postId, principal.getName());

        return "redirect:/posts/" + postId + "/comments";
    }

    @GetMapping("/{postId}/delete")
    @PreAuthorize("@postService.isOwner(#principal.name, #postId) or @postService.isAdmin(#principal.name)")
    public String deletePost(@PathVariable("postId") Long postId, Principal principal, HttpServletRequest request) {
        this.postService.deletePost(postId);

        return "redirect:/dashboard";
    }
}
