package bg.softuni.creddit.repository;

import bg.softuni.creddit.model.entity.Post;
import bg.softuni.creddit.model.entity.User;
import bg.softuni.creddit.model.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
