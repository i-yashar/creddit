package bg.softuni.creddit.web;

import bg.softuni.creddit.service.PostService;
import bg.softuni.creddit.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {
    private final PostService postService;
    private final UserService userService;

    public HomeController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping(path = {"", "/"})
    public String index(Model model) {
        return "index";
    }

    @GetMapping(path = {"/admins"})
    @PreAuthorize("@postService.isAdmin(#principal.name)")
    public String admin(Model model, Principal principal) {

        model.addAttribute("users", this.userService.retrieveAllUsers());

        return "admins";
    }
}
