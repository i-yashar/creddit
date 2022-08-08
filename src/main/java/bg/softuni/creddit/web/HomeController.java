package bg.softuni.creddit.web;

import bg.softuni.creddit.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(path = {"", "/"})
    @PreAuthorize("isAnonymous()")
    public String index(Model model) {
        return "index";
    }
}
