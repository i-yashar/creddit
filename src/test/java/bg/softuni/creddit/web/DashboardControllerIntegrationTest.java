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
public class DashboardControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("testUser");
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
    void testIndexPageForAuthenticatedUsers() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("posts", "roles"))
                .andExpect(view().name("dashboard"));
    }

    @Test
    void testIndexPageForUnauthenticatedUsers() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("posts"))
                .andExpect(view().name("dashboard"));
    }
}
