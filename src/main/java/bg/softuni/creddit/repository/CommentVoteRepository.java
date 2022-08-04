package bg.softuni.creddit.repository;

import bg.softuni.creddit.model.entity.CommentVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {
    @Query("SELECT cv FROM CommentVote cv WHERE cv.user.username = :username AND cv.comment.id = :id")
    CommentVote findCommentVoteByUsernameAndCommentId(@Param("username") String username,
                                                      @Param("id") Long id);
}
