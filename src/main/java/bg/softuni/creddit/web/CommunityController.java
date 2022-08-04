package bg.softuni.creddit.web;

import bg.softuni.creddit.model.dto.CreateCommunityDTO;
import bg.softuni.creddit.model.view.CommunityView;
import bg.softuni.creddit.service.CommunityService;
import bg.softuni.creddit.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class CommunityController {
    private final CommunityService communityService;
    private final PostService postService;

    public CommunityController(CommunityService communityService, PostService postService) {
        this.communityService = communityService;
        this.postService = postService;
    }

    @GetMapping("/community/{communityName}")
    public String communityPage(@PathVariable(name = "communityName") String communityName,
                                Model model) {

        model.addAttribute("community", this.communityService.getCommunity(communityName));
        model.addAttribute("posts", this.postService.retrieveAllPosts());
        return "community";
    }

    @GetMapping("/community/join/{communityName}")
    public String joinCommunity(@PathVariable(name = "communityName") String communityName,
                                Principal principal) {

        this.communityService.addUser(principal.getName(), communityName);

        return "redirect:/community/" + communityName;
    }

    @GetMapping("/community/create")
    public String createCommunity(Model model) {
        model.addAttribute("createCommunityDTO", new CreateCommunityDTO());
        return "create-community";
    }

    @PostMapping("/community/create")
    public String createCommunity(@Valid CreateCommunityDTO createCommunityDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  Principal principal) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("createCommunityDTO", createCommunityDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createCommunityDTO", bindingResult);
            return "redirect:/community/create";
        }

        this.communityService.createCommunity(createCommunityDTO, principal.getName());
        return "redirect:/community/" + createCommunityDTO.getName().substring(1);
    }
}
