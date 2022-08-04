package bg.softuni.creddit.repository;

import bg.softuni.creddit.model.entity.Post;
import bg.softuni.creddit.model.entity.User;
import bg.softuni.creddit.model.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserAndPost(User user, Post post);

    @Query("SELECT v FROM Vote v WHERE v.user.username = :username AND v.post.id = :id")
    Vote findVoteByUsernameAndPostId(@Param("username") String username, @Param("id") Long id);
}
