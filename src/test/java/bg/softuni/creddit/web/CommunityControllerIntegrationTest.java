package bg.softuni.creddit.web;

import bg.softuni.creddit.model.entity.Community;
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
public class CommunityControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private User testUser, testModerator, testAdmin;

    private Community testCommunity;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("testUser");
        testModerator = testDataUtils.createTestModerator("testModerator");
        testAdmin = testDataUtils.createTestAdmin("testAdmin");
        testCommunity = testDataUtils.createTestCommunity(testUser);
    }

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testAllCommunitiesAvailableToUnauthorizedVisitors() throws Exception {
        mockMvc.perform(get("/communities/all"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("all-communities"))
                .andExpect(model().attributeExists("communities"));
    }

    @Test
    @WithMockUser(
            username = "testUser"
    )
    void testCommunityPage() throws Exception {
        mockMvc.perform(get("/communities/{communityName}", testCommunity.getName().substring(1)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("community"))
                .andExpect(model().attributeExists("community", "posts"));
    }

    @Test
    @WithMockUser(
            username = "testModerator",
            roles = {"USER", "MODERATOR"}
    )
    void testJoinCommunity() throws Exception {
        mockMvc.perform(get("/communities/{communityName}/join", testCommunity.getName().substring(1)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/communities/" + testCommunity.getName().substring(1)));
    }

    @Test
    @WithMockUser(
            username = "testModerator",
            roles = {"USER", "MODERATOR"}
    )
    void testLeaveCommunity() throws Exception {
        testJoinCommunity();
        mockMvc.perform(get("/communities/{communityName}/leave", testCommunity.getName().substring(1)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/communities/" + testCommunity.getName().substring(1)));
    }

    @Test
    @WithMockUser(
            username = "testUser"
    )
    void testCreateCommunityPage() throws Exception {
        mockMvc.perform(get("/communities/create"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("create-community"))
                .andExpect(model().attributeExists("createCommunityDTO"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testCreateCommunity() throws Exception {

        mockMvc.perform(post("/communities/create")
                .sessionAttr("number", 4)
                .param("name", "_testCreateCommunity")
                .param("description", "test description")
                .param("communityPhotoUrl", "testPhoto://test.png")
                .param("answer", "even")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/communities/testCreateCommunity"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testCreateCommunity_CommunityAlreadyExists() throws Exception {

        mockMvc.perform(post("/communities/create")
                        .sessionAttr("number", 4)
                        .param("name", "_testCommunity")
                        .param("description", "test description")
                        .param("communityPhotoUrl", "testPhoto://test.png")
                        .param("answer", "even")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/community/create"));
    }
}
