package bg.softuni.creddit.web;

import bg.softuni.creddit.model.entity.Comment;
import bg.softuni.creddit.model.entity.Community;
import bg.softuni.creddit.model.entity.Post;
import bg.softuni.creddit.model.entity.User;
import bg.softuni.creddit.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private User testUser, testSecondUser, testModerator, testAdmin;

    private Community testCommunity;

    private Post testPost, testSecondPost;

    private Comment testComment, testSecondComment;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("testUser");
        testSecondUser = testDataUtils.createTestUser("testSecondUser");
        testModerator = testDataUtils.createTestModerator("testModerator");
        testAdmin = testDataUtils.createTestAdmin("testAdmin");

        testCommunity = testDataUtils.createTestCommunity(testUser);

        testPost = testDataUtils.createTestPost(testUser, testCommunity);
        testSecondPost = testDataUtils.createTestPost(testSecondUser, testCommunity);

        testComment = testDataUtils.createTestComment(testPost, testUser);
        testSecondComment = testDataUtils.createTestComment(testPost, testSecondUser);

    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testShowComments() throws Exception {
        mockMvc.perform(get("/posts/{postId}/comments", testPost.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("post", "comments", "roles"))
                .andExpect(view().name("post-comments"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testDeleteCommentIsSuccessful_isOwner() throws Exception {
        mockMvc.perform(get("/posts/{postId}/comments/{commentId}/delete", testPost.getId(), testComment.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + testPost.getId() + "/comments"));
    }

    @Test
    @WithMockUser(
            username = "testModerator",
            roles = {"USER", "MODERATOR"}
    )
    void testDeleteCommentIsSuccessful_isModerator() throws Exception {
        mockMvc.perform(get("/posts/{postId}/comments/{commentId}/delete", testPost.getId(), testComment.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + testPost.getId() + "/comments"));
    }

    @Test
    @WithMockUser(
            username = "testAdmin",
            roles = {"USER", "MODERATOR", "ADMIN"}
    )
    void testDeleteCommentIsSuccessful_isAdmin() throws Exception {
        mockMvc.perform(get("/posts/{postId}/comments/{commentId}/delete", testPost.getId(), testComment.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + testPost.getId() + "/comments"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testDeleteCommentIsForbidden_isNotOwner() throws Exception {
        mockMvc.perform(get("/posts/{postId}/comments/{commentId}/delete", testPost.getId(), testSecondComment.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testAddPostPage() throws Exception {
        mockMvc.perform(get("/posts/addPost"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-post"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testAddPostIsSuccessful() throws Exception {
        mockMvc.perform(post("/posts/addPost")
                        .param("title", "test post title")
                        .param("description", "test post description")
                        .param("community", testCommunity.getName())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testAddPostFail_InvalidData() throws Exception {
        mockMvc.perform(post("/posts/addPost")
                        .param("title", "test")
                        .param("description", "test")
                        .param("community", testCommunity.getName())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/addPost"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testAddCommentPage() throws Exception {
        mockMvc.perform(get("/posts/{postId}/addComment", testPost.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("add-comment"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testAddCommentIsSuccessful() throws Exception {
        mockMvc.perform(post("/posts/{postId}/addComment", testPost.getId())
                        .param("comment", "test comment")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + testPost.getId() + "/comments"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testAddCommentFail_InvalidData() throws Exception {
        mockMvc.perform(post("/posts/{postId}/addComment", testPost.getId())
                        .param("comment", "")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + testPost.getId() + "/addComment"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testDeletePostIsSuccessful_IsOwner() throws Exception {
        mockMvc.perform(get("/posts/{postId}/delete", testPost.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @WithMockUser(
            username = "testModerator",
            roles = {"USER", "MODERATOR"}
    )
    void testDeletePostIsSuccessful_IsModerator() throws Exception {
        mockMvc.perform(get("/posts/{postId}/delete", testPost.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @WithMockUser(
            username = "testAdmin",
            roles = {"USER", "MODERATOR", "ADMIN"}
    )
    void testDeletePostIsSuccessful_IsAdmin() throws Exception {
        mockMvc.perform(get("/posts/{postId}/delete", testPost.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testDeletePostIsForbidden_IsNotOwner() throws Exception {
        mockMvc.perform(get("/posts/{postId}/delete", testSecondPost.getId()))
                .andExpect(status().is4xxClientError());
    }
}
