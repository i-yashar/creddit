package bg.softuni.creddit.web;

import bg.softuni.creddit.service.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final PostService postService;

    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(path = {"", "/"})
    @PreAuthorize("isAnonymous()")
    public String index(Model model) {
        model.addAttribute("posts", this.postService.retrieveAllPosts());
        return "index";
    }

    @GetMapping("/test")
    public String test() {
        return "style-test";
    }
}