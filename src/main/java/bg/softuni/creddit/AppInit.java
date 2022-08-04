package bg.softuni.creddit;

import bg.softuni.creddit.model.entity.Post;
import bg.softuni.creddit.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppInit implements CommandLineRunner {
    private final PostRepository postRepository;

    public AppInit(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        List<Post> posts = new ArrayList<>();
//
//        for (int i = 0; i < 25; i++) {
//            Post post = new Post();
//            post.setDescription("some description " + i);
//            post.setTitle("some title " + i);
//            post.setUpvoteCount(i * 5);
//            posts.add(post);
//        }
//
//        this.postRepository.saveAll(posts);
    }
}
