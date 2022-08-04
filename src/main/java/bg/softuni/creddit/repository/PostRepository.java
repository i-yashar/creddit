package bg.softuni.creddit.repository;

import bg.softuni.creddit.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOwnerUsername(String username);
}
