package bg.softuni.creddit.web;

import bg.softuni.creddit.model.dto.UserProfileEditDTO;
import bg.softuni.creddit.service.PostService;
import bg.softuni.creddit.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PostService postService;

    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping(path = {"", "/"})
    public String profilePage(Model model, Principal principal) {
        model.addAttribute("user", this.userService.getUserProfileDetails());
        model.addAttribute("posts", this.postService.getAllPostsByUsername(principal.getName()));
        return "user-profile";
    }

    @GetMapping( "/{username}/profile")
    public String profilePage(@PathVariable(name = "username") String username, Model model) {
        model.addAttribute("user", this.userService.getUserProfileDetails(username));
        model.addAttribute("posts", this.postService.getAllPostsByUsername(username));
        return "user-profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        model.addAttribute("user", this.userService.getUserProfileDetails());
        return "edit-profile";
    }

    @PostMapping("/profile/edit")
    public String editProfile(@Valid UserProfileEditDTO userProfileEditDTO,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Principal principal) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", userProfileEditDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            return "redirect:/users/profile/edit";
        }

        this.userService.editUserProfile(userProfileEditDTO, principal.getName());

        return "redirect:/users/" + principal.getName() + "/profile";
    }
}
