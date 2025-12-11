package co.istad.identity.security;

import co.istad.identity.domain.Role;
import co.istad.identity.domain.User;
import co.istad.identity.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username)
        );

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .disabled(!user.getIsEnabled())
                .credentialsExpired(!user.getCredentialsNonExpired())
                .accountLocked(!user.getAccountNonLocked())
                .accountExpired(!user.getAccountNonExpired())
                .roles(user.getRoles().stream().map(Role::getName).toArray(String[]::new))
                .password(user.getPassword())
                .build();
    }
}
