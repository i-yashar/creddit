package bg.softuni.creddit.web;

import bg.softuni.creddit.model.dto.UserRegisterDTO;
import bg.softuni.creddit.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("registerModel")
    public UserRegisterDTO initUserRegisterDTO() {
        return new UserRegisterDTO();
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid UserRegisterDTO registerModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerModel", registerModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerModel", bindingResult);
            return "redirect:/users/register";
        }

        this.userService.registerAndLogin(registerModel);

        return "redirect:/";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            RedirectAttributes redirectAttributes
    ) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("bad_credentials", true);

        return "redirect:/users/login";
    }

}
