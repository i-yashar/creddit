package bg.softuni.creddit.web.rest;

import bg.softuni.creddit.service.CommunityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller("/api/community")
public class CommunityRestController {
    private final CommunityService communityService;

    public CommunityRestController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping("/join/{communityName}")
    public String joinCommunity(@PathVariable(name = "communityName") String communityName,
                                Principal principal) {

        this.communityService.addUser(principal.getName(), communityName);

        return "redirect:/community/" + communityName;
    }
}
