package bg.softuni.creddit.web.rest;

import bg.softuni.creddit.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@RestController
public class IndexController {
    @GetMapping("api/index")
    public ResponseEntity<User> index() {
        User user = new User();
        user.setEmail("someEmail@email.com");
        user.setCredits(15);
        user.setUsername("someUsername");
        user.setPosts(new ArrayList<>());
        return ResponseEntity.ok(user);
    }
}
