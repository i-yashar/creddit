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

import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CommunityControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private User testUser;

    private Community testCommunity;

    @BeforeEach
    void setUp() {
        testUser = testDataUtils.createTestUser("testUser");
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
            username = "testUser"
    )
    void testJoinCommunity() throws Exception {
        mockMvc.perform(get("/communities/{communityName}/join", testCommunity.getName().substring(1)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/communities/" + testCommunity.getName().substring(1)));
    }

    @Test
    @WithMockUser(
            username = "testUser"
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
}
