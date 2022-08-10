package bg.softuni.creddit.util;

import bg.softuni.creddit.model.entity.Community;
import bg.softuni.creddit.model.entity.User;
import bg.softuni.creddit.repository.CommunityRepository;
import bg.softuni.creddit.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class TestDataUtils {
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;

    public TestDataUtils(UserRepository userRepository, CommunityRepository communityRepository) {
        this.userRepository = userRepository;
        this.communityRepository = communityRepository;
    }

    public User createTestUser(String username) {
        User testUser = new User();
        testUser.setUsername(username);
        testUser.setPassword("12345");
        testUser.setEmail("test@test.com");
        testUser.setFirstName("test");
        testUser.setLastName("test");
        testUser.setCredits(5);

        return userRepository.save(testUser);
    }

    public Community createTestCommunity(User user) {
        Community testCommunity = new Community();
        testCommunity.setName("_testCommunity");
        testCommunity.setMembers(new HashSet<>());
        testCommunity.setCreatedBy(user);
        testCommunity.setDescription("test community");

        return communityRepository.save(testCommunity);
    }

    public void cleanUpDatabase() {
        communityRepository.deleteAll();
        userRepository.deleteAll();
    }
}
