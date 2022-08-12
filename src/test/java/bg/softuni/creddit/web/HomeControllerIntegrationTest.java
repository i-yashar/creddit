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
public class HomeControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private User testUser, testModerator, testAdmin;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("testUser");
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
    void testIndexPageAvailable_authenticatedUser() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("user", "communities"))
                .andExpect(view().name("index"));
    }

    @Test
    void testIndexPageAvailable_unauthenticatedVisitor() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser(
            username = "testAdmin",
            roles = {"USER", "MODERATOR", "ADMIN"}
    )
    void testAdminPageAvailable_toAdmin() throws Exception {
        mockMvc.perform(get("/admins"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("admins"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testAdminPageUnavailable_toUsers() throws Exception {
        mockMvc.perform(get("/admins"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(
            username = "testModerator",
            roles = {"USER", "MODERATOR"}
    )
    void testAdminPageUnavailable_toModerators() throws Exception {
        mockMvc.perform(get("/admins"))
                .andExpect(status().is4xxClientError());
    }
}
