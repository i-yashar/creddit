package bg.softuni.creddit.web;

import bg.softuni.creddit.model.dto.AddPostDTO;
import bg.softuni.creddit.service.PostService;
import bg.softuni.creddit.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final PostService postService;

    public DashboardController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping(path = {"", "/"})
    public String index(Model model) {
        model.addAttribute("posts", this.postService.retrieveAllPosts());
        return "dashboard";
    }

}
