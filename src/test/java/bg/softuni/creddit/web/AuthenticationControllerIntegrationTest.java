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
public class AuthenticationControllerIntegrationTest {
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
    void testLoginPage() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testLoginPageFail_AuthenticatedUser() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testRegisterPage() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("register"));
    }

    @Test
    @WithMockUser(
            username = "testUser",
            roles = "USER"
    )
    void testRegisterPageFail_AuthenticatedUser() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testUserRegisterIsSuccessful() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", "testRegistrationUser")
                        .param("email", "testRegistrationUser@test.com")
                        .param("firstName", "test")
                        .param("lastName", "test")
                        .param("password", "12345")
                        .param("confirmPassword", "12345")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testUserRegisterFail_InvalidData() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", "-")
                        .param("email", "testRegistrationUser@test.com")
                        .param("firstName", "test")
                        .param("lastName", "test")
                        .param("password", "12345")
                        .param("confirmPassword", "12345")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"));
    }
}
