package bg.softuni.creddit.util;

import bg.softuni.creddit.model.entity.*;
import bg.softuni.creddit.model.entity.enums.UserRoleEnum;
import bg.softuni.creddit.repository.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class TestDataUtils {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final CommentVoteRepository commentVoteRepository;

    public TestDataUtils(UserRepository userRepository, UserRoleRepository userRoleRepository, CommunityRepository communityRepository, CommentRepository commentRepository, PostRepository postRepository, VoteRepository voteRepository, CommentVoteRepository commentVoteRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.communityRepository = communityRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.voteRepository = voteRepository;
        this.commentVoteRepository = commentVoteRepository;
    }

    public void initRoles() {
        if(userRoleRepository.count() == 0) {
            UserRole admin = new UserRole(UserRoleEnum.ADMIN);
            UserRole moderator = new UserRole(UserRoleEnum.MODERATOR);
            UserRole user = new UserRole(UserRoleEnum.USER);

            userRoleRepository.save(admin);
            userRoleRepository.save(moderator);
            userRoleRepository.save(user);
        }
    }

    public User createTestUser(String username) {
        initRoles();

        User testUser = new User();
        testUser.setUsername(username);
        testUser.setPassword("12345");
        testUser.setEmail(username + "@test.com");
        testUser.setFirstName("User");
        testUser.setLastName("test");
        testUser.setCredits(5);
        testUser.setUserRoles(userRoleRepository.findAll().stream()
                .filter(r -> r.getUserRole() == UserRoleEnum.USER)
                .collect(Collectors.toSet()));

        return userRepository.save(testUser);
    }

    public User createTestModerator(String username) {
        initRoles();

        User testModerator = new User();
        testModerator.setUsername(username);
        testModerator.setPassword("12345");
        testModerator.setEmail(username + "@test.com");
        testModerator.setFirstName("Moderator");
        testModerator.setLastName("test");
        testModerator.setCredits(5);
        testModerator.setUserRoles(userRoleRepository.findAll().stream()
                .filter(r -> r.getUserRole() != UserRoleEnum.ADMIN)
                .collect(Collectors.toSet()));

        return userRepository.save(testModerator);
    }

    public User createTestAdmin(String username) {
        initRoles();

        User testAdmin = new User();
        testAdmin.setUsername(username);
        testAdmin.setPassword("12345");
        testAdmin.setEmail(username + "@test.com");
        testAdmin.setFirstName("Admin");
        testAdmin.setLastName("test");
        testAdmin.setCredits(5);
        testAdmin.setUserRoles(new HashSet<>(userRoleRepository.findAll()));

        return userRepository.save(testAdmin);
    }

    public Community createTestCommunity(User... users) {
        Community testCommunity = new Community();
        testCommunity.setName("_testCommunity");
        testCommunity.setMembers(new HashSet<>(Arrays.stream(users).toList()));
        testCommunity.setCreatedBy(users[0]);
        testCommunity.setDescription("test community");

        return communityRepository.save(testCommunity);
    }

    public Post createTestPost(User owner, Community community) {
        Post post = new Post();
        post.setOwner(owner);
        post.setCommunity(community);
        post.setTitle("test post");
        post.setDescription("test description");
        post.setUpvoteCount(1);

        return postRepository.save(post);
    }

    public Vote createTestVote(User user, Post post) {
        Vote vote = new Vote(user, post, 1);

        return voteRepository.save(vote);
    }

    public Comment createTestComment(Post post, User owner) {
        Comment comment = new Comment("Test comment", 1, post, owner);

        return commentRepository.save(comment);
    }

    public CommentVote createTestCommentVote(User user, Comment comment) {
        CommentVote commentVote = new CommentVote(user, comment, 1);

        return commentVoteRepository.save(commentVote);
    }

    public void cleanUpDatabase() {
        commentVoteRepository.deleteAll();
        commentRepository.deleteAll();
        voteRepository.deleteAll();
        postRepository.deleteAll();
        communityRepository.deleteAll();
        userRepository.deleteAll();
        userRoleRepository.deleteAll();
    }
}
