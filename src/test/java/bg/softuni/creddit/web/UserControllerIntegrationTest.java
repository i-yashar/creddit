package bg.softuni.creddit.web;

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
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private User testUser, testSecondUser, testModerator, testAdmin;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("testUser");
        testSecondUser = testDataUtils.createTestUser("testSecondUser");
        testModerator = testDataUtils.createTestModerator("testModerator");
        testAdmin = testDataUtils.createTestAdmin("testAdmin");
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
    void testProfilePage_CurrentUser() throws Exception {
        mockMvc.perform(get("/users/"))
                .andExpect(model().attributeExists("user", "posts"))
                .andExpect(view().name("user-profile"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testProfilePage_AnotherUser() throws Exception {
        mockMvc.perform(get("/users/{username}/profile", testSecondUser.getUsername()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("user", "posts"))
                .andExpect(view().name("user-profile"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testEditProfilePage() throws Exception {
        mockMvc.perform(get("/users/profile/edit"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("edit-profile"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testEditProfileIsSuccessful() throws Exception {
        mockMvc.perform(post("/users/profile/edit")
                        .param("firstName", "test")
                        .param("lastName", "testov")
                        .param("username", "testEditedUsername")
                        .param("profilePicUrl", "testUrl://testPic.png")
                        .param("about", "test about")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/" + testUser.getUsername() + "/profile"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testEditProfileFail_InvalidData() throws Exception {
        mockMvc.perform(post("/users/profile/edit")
                        .param("firstName", "test")
                        .param("lastName", "testov")
                        .param("username", "testSecondUser")
                        .param("profilePicUrl", "testUrl://testPic.png")
                        .param("about", "test about")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile/edit"));
    }

    @Test
    @WithMockUser(
            username = "testAdmin",
            roles = {"USER", "MODERATOR", "ADMIN"}
    )
    void testMakeModeratorIsSuccessful() throws Exception {
        mockMvc.perform(get("/users/{username}/makeModerator", testUser.getUsername()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admins"));
    }

    @Test
    @WithMockUser(
            username = "testAdmin",
            roles = {"USER", "MODERATOR", "ADMIN"}
    )
    void testRevokeModeratorIsSuccessful() throws Exception {
        mockMvc.perform(get("/users/{username}/revokeModerator", testModerator.getUsername()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admins"));
    }
}
