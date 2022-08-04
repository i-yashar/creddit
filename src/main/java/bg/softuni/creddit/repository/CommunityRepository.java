package bg.softuni.creddit.repository;

import bg.softuni.creddit.model.entity.Community;
import bg.softuni.creddit.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Optional<Community> findByName(String name);

    @Query("SELECT c.members FROM Community c WHERE c.name = :communityName")
    List<User> getAllCommunityMembers(@Param("communityName") String communityName);

    Optional<Community> getCommunityByNameAndMembersContaining(String community, User user);
}
