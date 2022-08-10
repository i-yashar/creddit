package bg.softuni.creddit.service;

import bg.softuni.creddit.model.dto.CommentVoteDTO;
import bg.softuni.creddit.model.entity.*;
import bg.softuni.creddit.repository.CommentRepository;
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
public class CommentServiceTest {
    @Mock
    private CommentRepository mockCommentRepository;

    @Mock
    private CommentVoteService mockCommentVoteService;

    @Mock
    private UserService mockUserService;

    @Mock
    private ModelMapper mockModelMapper;

    private User testUser;

    private Post testPost;

    private Comment testComment;

    private CommentVote testCommentVote;

    private CommentService toTest;

    @BeforeEach
    void setUp() {
        toTest = new CommentService(mockCommentRepository, mockCommentVoteService, mockUserService, mockModelMapper);

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

        testComment = new Comment("test comment", 0, testPost, testUser);
        testComment.setId((long) 1);

        testCommentVote = new CommentVote();
        testCommentVote.setId((long) 1);
        testCommentVote.setValue(0);
        testCommentVote.setComment(testComment);
        testCommentVote.setUser(testUser);
    }

    @Test
    void testAddComment() {
        Mockito.when(mockCommentRepository.findById((long) 1)).thenReturn(Optional.of(testComment));

        toTest.addComment("test comment", testPost, testUser);

        Comment actual = mockCommentRepository.findById((long) 1).get();

        Assertions.assertEquals("test comment", actual.getContent());
    }

    @Test
    void testUpVoteComment_NoVote() {
        testComment.setUpvoteCount(0);
        testCommentVote.setValue(0);

        Mockito.when(mockCommentRepository.findById(testComment.getId())).thenReturn(Optional.of(testComment));
        Mockito.when(mockCommentVoteService.findCommentVoteByUserAndComment(testUser, testComment)).thenReturn(testCommentVote);
        Mockito.when(mockUserService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);

        CommentVoteDTO actual = toTest.upVoteComment(testUser.getUsername(), testComment.getId());

        Assertions.assertEquals(1, actual.getUpVoteCount());
        Assertions.assertEquals(1, testCommentVote.getValue());
    }

    @Test
    void testUpVoteComment_AlreadyUpVoted() {
        testComment.setUpvoteCount(1);
        testCommentVote.setValue(1);

        Mockito.when(mockCommentRepository.findById(testComment.getId())).thenReturn(Optional.of(testComment));
        Mockito.when(mockCommentVoteService.findCommentVoteByUserAndComment(testUser, testComment)).thenReturn(testCommentVote);
        Mockito.when(mockUserService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);

        CommentVoteDTO actual = toTest.upVoteComment(testUser.getUsername(), testComment.getId());

        Assertions.assertEquals(0, actual.getUpVoteCount());
        Assertions.assertEquals(0, testCommentVote.getValue());
    }

    @Test
    void testUpVoteComment_PreviouslyDownVoted() {
        testComment.setUpvoteCount(-1);
        testCommentVote.setValue(-1);

        Mockito.when(mockCommentRepository.findById(testComment.getId())).thenReturn(Optional.of(testComment));
        Mockito.when(mockCommentVoteService.findCommentVoteByUserAndComment(testUser, testComment)).thenReturn(testCommentVote);
        Mockito.when(mockUserService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);

        CommentVoteDTO actual = toTest.upVoteComment(testUser.getUsername(), testComment.getId());

        Assertions.assertEquals(1, actual.getUpVoteCount());
        Assertions.assertEquals(1, testCommentVote.getValue());
    }

    @Test
    void testDownVoteComment_NoVote() {
        testComment.setUpvoteCount(0);
        testCommentVote.setValue(0);

        Mockito.when(mockCommentRepository.findById(testComment.getId())).thenReturn(Optional.of(testComment));
        Mockito.when(mockCommentVoteService.findCommentVoteByUserAndComment(testUser, testComment)).thenReturn(testCommentVote);
        Mockito.when(mockUserService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);

        CommentVoteDTO actual = toTest.downVoteComment(testUser.getUsername(), testComment.getId());

        Assertions.assertEquals(-1, actual.getUpVoteCount());
        Assertions.assertEquals(-1, testCommentVote.getValue());
    }

    @Test
    void testDownVoteComment_AlreadyDownVoted() {
        testComment.setUpvoteCount(-1);
        testCommentVote.setValue(-1);

        Mockito.when(mockCommentRepository.findById(testComment.getId())).thenReturn(Optional.of(testComment));
        Mockito.when(mockCommentVoteService.findCommentVoteByUserAndComment(testUser, testComment)).thenReturn(testCommentVote);
        Mockito.when(mockUserService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);

        CommentVoteDTO actual = toTest.downVoteComment(testUser.getUsername(), testComment.getId());

        Assertions.assertEquals(0, actual.getUpVoteCount());
        Assertions.assertEquals(0, testCommentVote.getValue());
    }

    @Test
    void testDownVoteComment_PreviouslyUpVoted() {
        testComment.setUpvoteCount(1);
        testCommentVote.setValue(1);

        Mockito.when(mockCommentRepository.findById(testComment.getId())).thenReturn(Optional.of(testComment));
        Mockito.when(mockCommentVoteService.findCommentVoteByUserAndComment(testUser, testComment)).thenReturn(testCommentVote);
        Mockito.when(mockUserService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);

        CommentVoteDTO actual = toTest.downVoteComment(testUser.getUsername(), testComment.getId());

        Assertions.assertEquals(-1, actual.getUpVoteCount());
        Assertions.assertEquals(-1, testCommentVote.getValue());
    }
}
