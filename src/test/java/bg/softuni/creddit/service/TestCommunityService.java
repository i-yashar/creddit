package bg.softuni.creddit.service;

import bg.softuni.creddit.exception.notfound.CommunityNotFoundException;
import bg.softuni.creddit.model.dto.CreateCommunityDTO;
import bg.softuni.creddit.model.entity.Community;
import bg.softuni.creddit.model.entity.User;
import bg.softuni.creddit.repository.CommunityRepository;
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
public class TestCommunityService {
    @Mock
    private CommunityRepository mockCommunityRepository;

    @Mock
    private UserService mockUserService;

    @Mock
    private ModelMapper mockModelMapper;

    private Community testCommunity;

    private User testUser;

    private CreateCommunityDTO testCreateCommunityDTO;

    private CommunityService toTest;

    @BeforeEach
    void setUp() {
        toTest = new CommunityService(mockCommunityRepository, mockUserService, mockModelMapper);

        testCommunity = new Community();
        testCommunity.setId((long) 1);
        testCommunity.setName("_testCommunity");
        testCommunity.setDescription("test description");

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

        testCreateCommunityDTO = new CreateCommunityDTO();
    }

    @Test
    void testGetCommunityByName_BeginsWithUnderscore() {
        testCommunity.setName("_testCommunity");

        Mockito.when(mockCommunityRepository.findByName("_testCommunity")).thenReturn(Optional.of(testCommunity));

        Community actual = toTest.getCommunityByName("_testCommunity");

        Assertions.assertEquals("_testCommunity", actual.getName());
    }

    @Test
    void testGetCommunityByName_DoesntBeginWithUnderscore() {
        testCommunity.setName("_testCommunity");

        Mockito.when(mockCommunityRepository.findByName("_testCommunity")).thenReturn(Optional.of(testCommunity));

        Community actual = toTest.getCommunityByName("testCommunity");

        Assertions.assertEquals("_testCommunity", actual.getName());
    }

    @Test
    void testGetCommunityByNameThrowsWhenCommunityNotFound() {
        Assertions.assertThrows(CommunityNotFoundException.class, () -> toTest.getCommunityByName("not-existent test community"));
    }

    @Test
    void testAddUser() {
        Mockito.when(mockUserService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);
        Mockito.when(mockCommunityRepository.findByName(testCommunity.getName())).thenReturn(Optional.of(testCommunity));

        toTest.addUser(testUser.getUsername(), testCommunity.getName());

        Assertions.assertNotNull(testCommunity.getMembers());
        Assertions.assertEquals(1, testCommunity.getMembers().size());
        Assertions.assertTrue(testCommunity.getMembers().contains(testUser));
    }

    @Test
    void testRemoveUser() {

        Mockito.when(mockUserService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);
        Mockito.when(mockCommunityRepository.findByName(testCommunity.getName())).thenReturn(Optional.of(testCommunity));

        toTest.addUser(testUser.getUsername(), testCommunity.getName());
        toTest.removeUser(testUser.getUsername(), testCommunity.getName());

        Assertions.assertNotNull(testCommunity.getMembers());
        Assertions.assertEquals(0, testCommunity.getMembers().size());
        Assertions.assertFalse(testCommunity.getMembers().contains(testUser));
    }

    @Test
    void testCreateCommunity() {
        Mockito.when(mockModelMapper.map(testCreateCommunityDTO, Community.class)).thenReturn(testCommunity);
        Mockito.when(mockUserService.getUserByUsername(testUser.getUsername())).thenReturn(testUser);
        Mockito.when(mockCommunityRepository.findByName(testCommunity.getName())).thenReturn(Optional.of(testCommunity));

        toTest.createCommunity(testCreateCommunityDTO, testUser.getUsername());

        Assertions.assertNotNull(testCommunity.getCreatedBy());
        Assertions.assertEquals(testUser, testCommunity.getCreatedBy());
        Assertions.assertNotNull(testCommunity.getMembers());
        Assertions.assertEquals(1, testCommunity.getMembers().size());
        Assertions.assertTrue(testCommunity.getMembers().contains(testUser));
    }
}
