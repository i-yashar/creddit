package bg.softuni.creddit.service;

import bg.softuni.creddit.model.dto.PostVoteDTO;
import bg.softuni.creddit.model.entity.Community;
import bg.softuni.creddit.model.entity.Post;
import bg.softuni.creddit.model.entity.User;
import bg.softuni.creddit.model.entity.Vote;
import bg.softuni.creddit.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private CommentService mockCommentService;

    @Mock
    private UserService mockUserService;

    @Mock
    private VoteService mockVoteService;

    @Mock
    private CommentVoteService mockCommentVoteService;

    @Mock
    private PostRepository mockPostRepository;

    @Mock
    private CommunityService mockCommunityService;

    @Mock
    private ModelMapper mockModelMapper;

    private User testUser;

    private Post testPost;

    private Vote testVote;

    private PostService toTest;

    @BeforeEach
    void setUp() {
        toTest = new PostService(mockCommentService, mockUserService, mockVoteService, mockCommentVoteService, mockPostRepository, mockCommunityService, mockModelMapper);

        testUser = new User();
        testUser.setId((long) 1);
        testUser.setUsername("test user");
        testUser.setPassword("12345");
        testUser.setEmail("test@test.com");
        testUser.setFirstName("test");
        testUser.setLastName("test");
        testUser.setCredits(5);

        testPost = new Post();
        testPost.setId((long) 1);
        testPost.setUpvoteCount(0);
        testPost.setCommunity(new Community());
        testPost.setTitle("test title");
        testPost.setDescription("testDescription");
        testPost.setOwner(testUser);

        testVote = new Vote(testUser, testPost, 0);
        testVote.setId((long) 1);
    }

    @Test
    void testUpVotePost_NoVote() {
        testPost.setUpvoteCount(0);
        testVote.setValue(0);

        Mockito.when(mockPostRepository.findById(testPost.getId())).thenReturn(Optional.of(testPost));
        Mockito.when(mockUserService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);
        Mockito.when(mockVoteService.findVoteByUserAndPost(testUser, testPost)).thenReturn(testVote);

        PostVoteDTO actual = toTest.upVotePost(testUser.getUsername(), testPost.getId());

        Assertions.assertEquals(1, actual.getUpVoteCount());
        Assertions.assertEquals(1, testVote.getValue());
    }

    @Test
    void testUpVotePost_AlreadyUpVoted() {
        testPost.setUpvoteCount(1);
        testVote.setValue(1);

        Mockito.when(mockPostRepository.findById(testPost.getId())).thenReturn(Optional.of(testPost));
        Mockito.when(mockUserService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);
        Mockito.when(mockVoteService.findVoteByUserAndPost(testUser, testPost)).thenReturn(testVote);

        PostVoteDTO actual = toTest.upVotePost(testUser.getUsername(), testPost.getId());

        Assertions.assertEquals(0, actual.getUpVoteCount());
        Assertions.assertEquals(0, testVote.getValue());
    }

    @Test
    void testUpVotePost_PreviouslyDownVoted() {
        testPost.setUpvoteCount(-1);
        testVote.setValue(-1);

        Mockito.when(mockPostRepository.findById(testPost.getId())).thenReturn(Optional.of(testPost));
        Mockito.when(mockUserService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);
        Mockito.when(mockVoteService.findVoteByUserAndPost(testUser, testPost)).thenReturn(testVote);

        PostVoteDTO actual = toTest.upVotePost(testUser.getUsername(), testPost.getId());

        Assertions.assertEquals(1, actual.getUpVoteCount());
        Assertions.assertEquals(1, testVote.getValue());
    }

    @Test
    void testDownVotePost_NoVote() {
        testPost.setUpvoteCount(0);
        testVote.setValue(0);

        Mockito.when(mockPostRepository.findById(testPost.getId())).thenReturn(Optional.of(testPost));
        Mockito.when(mockUserService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);
        Mockito.when(mockVoteService.findVoteByUserAndPost(testUser, testPost)).thenReturn(testVote);

        PostVoteDTO actual = toTest.downVotePost(testUser.getUsername(), testPost.getId());

        Assertions.assertEquals(-1, actual.getUpVoteCount());
        Assertions.assertEquals(-1, testVote.getValue());
    }

    @Test
    void testDownVotePost_AlreadyDownVoted() {
        testPost.setUpvoteCount(-1);
        testVote.setValue(-1);

        Mockito.when(mockPostRepository.findById(testPost.getId())).thenReturn(Optional.of(testPost));
        Mockito.when(mockUserService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);
        Mockito.when(mockVoteService.findVoteByUserAndPost(testUser, testPost)).thenReturn(testVote);

        PostVoteDTO actual = toTest.downVotePost(testUser.getUsername(), testPost.getId());

        Assertions.assertEquals(0, actual.getUpVoteCount());
        Assertions.assertEquals(0, testVote.getValue());
    }

    @Test
    void testUpVotePost_PreviouslyUpVoted() {
        testPost.setUpvoteCount(1);
        testVote.setValue(1);

        Mockito.when(mockPostRepository.findById(testPost.getId())).thenReturn(Optional.of(testPost));
        Mockito.when(mockUserService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);
        Mockito.when(mockVoteService.findVoteByUserAndPost(testUser, testPost)).thenReturn(testVote);

        PostVoteDTO actual = toTest.downVotePost(testUser.getUsername(), testPost.getId());

        Assertions.assertEquals(-1, actual.getUpVoteCount());
        Assertions.assertEquals(-1, testVote.getValue());
    }
}
