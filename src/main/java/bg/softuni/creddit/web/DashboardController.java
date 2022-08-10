package bg.softuni.creddit.web;

import bg.softuni.creddit.model.dto.AddPostDTO;
import bg.softuni.creddit.service.PostService;
import bg.softuni.creddit.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final PostService postService;

    public DashboardController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping(path = {"", "/"})
    public String index(Model model,
                        @PageableDefault(
                                size = 5,
                                page = 0
                        ) Pageable pageable,
                        Principal principal) {
        model.addAttribute("posts", this.postService.retrieveAllPostsPaginationEnabled(pageable));
        model.addAttribute("roles", SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return "dashboard";
    }

}
