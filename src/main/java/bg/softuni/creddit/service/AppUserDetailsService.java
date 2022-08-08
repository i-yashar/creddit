package bg.softuni.creddit.service;

import bg.softuni.creddit.model.entity.User;
import bg.softuni.creddit.model.entity.UserRole;
import bg.softuni.creddit.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return userRepository.
                findByUsername(username).
                map(this::mapToUser).
                orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));
    }

    private UserDetails mapToUser(User user) {

        return org.springframework.security.core.userdetails
                .User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user
                        .getUserRoles()
                        .stream()
                        .map(this::map)
                        .toList())
                .build();
    }

    private GrantedAuthority map(UserRole userRole) {
        return new SimpleGrantedAuthority("ROLE_" +
                userRole.
                        getUserRole().name());
    }
}
