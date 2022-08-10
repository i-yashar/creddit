package bg.softuni.creddit.service;

import bg.softuni.creddit.model.entity.*;
import bg.softuni.creddit.repository.CommentVoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestCommentVoteService {
    @Mock
    private CommentVoteRepository mockCommentVoteRepository;

    private User testUser;

    private Post testPost;

    private Comment testComment;

    private CommentVote testCommentVote;

    private CommentVoteService toTest;

    @BeforeEach
    void setUp() {
        toTest = new CommentVoteService(mockCommentVoteRepository);

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
    void testFindCommentVoteByUserAndComment_CommentVoteExists() {
        Mockito.when(mockCommentVoteRepository.findCommentVoteByUsernameAndCommentId(testUser.getUsername(), testComment.getId()))
                .thenReturn(testCommentVote);

        CommentVote actual = toTest.findCommentVoteByUserAndComment(testUser, testComment);

        Assertions.assertEquals(testUser.getUsername(), actual.getUser().getUsername());
        Assertions.assertEquals(testComment.getContent(), actual.getComment().getContent());
    }

    @Test
    void testFindCommentVoteByUserAndComment_CommentVoteDoesntExist() {

        Mockito.when(mockCommentVoteRepository.findCommentVoteByUsernameAndCommentId(testUser.getUsername(), testComment.getId()))
                .thenReturn(null);

        CommentVote actual = toTest.findCommentVoteByUserAndComment(testUser, testComment);

        Assertions.assertNull(actual);
    }
}
