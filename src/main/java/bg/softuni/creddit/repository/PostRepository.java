package bg.softuni.creddit.repository;

import bg.softuni.creddit.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByOrderByCreatedOnDesc(Pageable pageable);
    Page<Post> findByCommunityIdInOrderByCreatedOnDesc(Collection<Long> communities, Pageable pageable);
    List<Post> findAllByOwnerUsername(String username);
    Page<Post> findAllByCommunityNameOrderByCreatedOnDesc(String communityName, Pageable pageable);
}
