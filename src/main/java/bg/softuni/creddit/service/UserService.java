package bg.softuni.creddit.service;

import bg.softuni.creddit.exception.notfound.UserNotFoundException;
import bg.softuni.creddit.model.dto.UserProfileEditDTO;
import bg.softuni.creddit.model.entity.User;
import bg.softuni.creddit.model.dto.UserRegisterDTO;
import bg.softuni.creddit.model.entity.UserRole;
import bg.softuni.creddit.model.entity.enums.UserRoleEnum;
import bg.softuni.creddit.model.view.UserAdminView;
import bg.softuni.creddit.model.view.UserProfileView;
import bg.softuni.creddit.repository.UserRepository;
import bg.softuni.creddit.repository.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void editUserProfile(UserProfileEditDTO userProfileEditDTO, String username) {

        User user = this.getUserByUsername(username);

        user.setFirstName(userProfileEditDTO.getFirstName());
        user.setLastName(userProfileEditDTO.getLastName());
        user.setUsername(userProfileEditDTO.getUsername());
        user.setProfilePicUrl(userProfileEditDTO.getProfilePicUrl());
        user.setAbout(userProfileEditDTO.getAbout());

        this.userRepository.save(user);

        login(user);
    }

    public UserProfileView getUserProfileDetails(String username) {
        return modelMapper.map(this.getUserByUsername(username), UserProfileView.class);
    }

    public List<UserAdminView> retrieveAllUsers() {
        return this.userRepository.findAll().stream()
                .map(u -> {
                    List<String> roles = u.getUserRoles().stream()
                            .map(ur -> ur.getUserRole().name())
                            .collect(Collectors.toList());

                    return new UserAdminView(u.getUsername(), roles);
                })
                .collect(Collectors.toList());
    }

    public void registerAndLogin(UserRegisterDTO userRegisterDTO) {
        User user = modelMapper.map(userRegisterDTO, User.class);

        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setRegisteredOn(LocalDateTime.now());
        user.setCredits(0);
        user.setAbout("You don't have any info in your about section yet. Click edit profile and type something interesting about yourself :)");
        user.setProfilePicUrl("https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&cs=tinysrgb&fit=crop&h=128&w=128&s=ee8bbf5fb8d6e43aaaa238feae2fe90d");

        userRepository.save(user);
        login(user);
    }

    private void login(User user) {
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(user.getUsername());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }

    protected User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof String) {
            return null;
        }
        String username = ((UserDetails) principal).getUsername();

        return this.getUserByUsername(username);
    }

    protected User getUserByUsername(String username) {
        return this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found."));
    }

    @Transactional
    public void promoteToModerator(String username) {
        User user = this.getUserByUsername(username);

        UserRole moderator = this.userRoleRepository.findByUserRoleLike(UserRoleEnum.MODERATOR).get();

        user.getUserRoles().add(moderator);

        this.userRepository.save(user);
    }

    @Transactional
    public void demoteToUser(String username) {
        User user = this.getUserByUsername(username);

        UserRole moderator = this.userRoleRepository.findByUserRoleLike(UserRoleEnum.MODERATOR).get();

        user.getUserRoles().remove(moderator);

        this.userRepository.save(user);
    }
}
