package bg.softuni.creddit.web;

import bg.softuni.creddit.service.CommunityService;
import bg.softuni.creddit.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {
    private final UserService userService;
    private final CommunityService communityService;

    public HomeController(UserService userService, CommunityService communityService) {
        this.userService = userService;
        this.communityService = communityService;
    }

    @GetMapping(path = {"", "/"})
    public String index(Model model, Principal principal) {
        if(principal != null) {
            model.addAttribute("user", principal.getName());
            model.addAttribute("communities", this.communityService.getUserCommunities(principal.getName()));
        }
        return "index";
    }

    @GetMapping(path = {"/admins"})
    @PreAuthorize("@postService.isAdmin(#principal.name)")
    public String admin(Model model, Principal principal) {

        model.addAttribute("users", this.userService.retrieveAllUsers());

        return "admins";
    }
}
