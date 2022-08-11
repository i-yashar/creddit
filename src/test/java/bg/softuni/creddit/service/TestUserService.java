package bg.softuni.creddit.service;

import bg.softuni.creddit.model.dto.UserProfileEditDTO;
import bg.softuni.creddit.model.entity.User;
import bg.softuni.creddit.repository.UserRepository;
import bg.softuni.creddit.repository.UserRoleRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TestUserService {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private UserRoleRepository mockuserRoleRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private UserDetailsService mockUserDetailsService;
    @Mock
    private ModelMapper mockModelMapper;

    private User testUser;

    private UserProfileEditDTO testUserProfileEditDTO;

    private UserService toTest;

    @BeforeEach
    void setUp() {
        toTest = new UserService(mockUserRepository, mockuserRoleRepository, mockPasswordEncoder, mockUserDetailsService, mockModelMapper);

        testUser = new User();
        testUser.setId((long) 1);
        testUser.setUsername("test user");
        testUser.setPassword("12345");
        testUser.setEmail("test@test.com");
        testUser.setFirstName("test");
        testUser.setLastName("test");
        testUser.setCredits(5);
        testUser.setProfilePicUrl("https://testProfilePic.com/testProfilePic.png");
        testUser.setAbout("test about");

        testUserProfileEditDTO = new UserProfileEditDTO();
        testUserProfileEditDTO.setUsername("updated test user");
        testUserProfileEditDTO.setFirstName("updated test");
        testUserProfileEditDTO.setLastName("updated test");
        testUserProfileEditDTO.setProfilePicUrl("updatedProfilePicUrl");
        testUserProfileEditDTO.setAbout("updated test about");
    }

    @Test
    void testEditUserProfileAndAssertThereIsCurrentUserSession() {
        Mockito.when(mockUserRepository.findByUsername(testUserProfileEditDTO.getUsername())).thenReturn(Optional.of(testUser));
        Mockito.when(mockUserRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        Mockito.when(mockUserDetailsService.loadUserByUsername(testUserProfileEditDTO.getUsername()))
                .thenReturn(org.springframework.security.core.userdetails
                .User.builder()
                .username(testUserProfileEditDTO.getUsername())
                .password(testUser.getPassword())
                .authorities("ROLE_ADMIN", "ROLE_USER")
                .build());

        toTest.editUserProfile(testUserProfileEditDTO, testUser.getUsername());

        Assertions.assertEquals("updated test user", testUser.getUsername());
        Assertions.assertEquals("updated test", testUser.getFirstName());
        Assertions.assertEquals("updated test", testUser.getLastName());
        Assertions.assertEquals("updatedProfilePicUrl", testUser.getProfilePicUrl());
        Assertions.assertEquals("updated test about", testUser.getAbout());
        Assertions.assertEquals(testUser, toTest.getCurrentUser());
    }
}
