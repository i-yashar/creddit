package bg.softuni.creddit.web;

import bg.softuni.creddit.model.dto.CreateCommunityDTO;
import bg.softuni.creddit.model.view.CommunityView;
import bg.softuni.creddit.service.CommunityService;
import bg.softuni.creddit.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.parameters.P;
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
@RequestMapping("/communities")
public class CommunityController {
    private final CommunityService communityService;
    private final PostService postService;

    public CommunityController(CommunityService communityService, PostService postService) {
        this.communityService = communityService;
        this.postService = postService;
    }

    @GetMapping("/{communityName}")
    public String communityPage(@PathVariable(name = "communityName") String communityName,
                                Model model,
                                @PageableDefault(
                                        size = 5,
                                        page = 0
                                ) Pageable pageable) {

        model.addAttribute("community", this.communityService.getCommunity(communityName));
        model.addAttribute("posts", this.postService.retrieveAllPostsPaginationEnabled(pageable));
        return "community";
    }

    @GetMapping("/all")
    public String allCommunities(Model model) {
        model.addAttribute("communities", this.communityService.getAllCommunities());

        return "all-communities";
    }

    @GetMapping("/{communityName}/join")
    public String joinCommunity(@PathVariable(name = "communityName") String communityName,
                                Principal principal) {

        this.communityService.addUser(principal.getName(), communityName);

        return "redirect:/communities/" + communityName;
    }

    @GetMapping("/{communityName}/leave")
    public String leaveCommunity(@PathVariable(name = "communityName") String communityName,
                                 Principal principal) {
        this.communityService.removeUser(principal.getName(), communityName);

        return "redirect:/communities/" + communityName;
    }

    @GetMapping("/create")
    public String createCommunity(Model model) {
        model.addAttribute("createCommunityDTO", new CreateCommunityDTO());
        return "create-community";
    }

    @PostMapping("/create")
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
        return "redirect:/communities/" + createCommunityDTO.getName().substring(1);
    }
}
