package co.istad.identity.security;

import co.istad.identity.domain.User;
import co.istad.identity.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username)
        );

        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            role.getPermissions().forEach(permission -> {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            });
        });

        log.info("USER AUTHORITIES: {}", authorities);

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .disabled(!user.getIsEnabled())
                .credentialsExpired(!user.getCredentialsNonExpired())
                .accountLocked(!user.getAccountNonLocked())
                .accountExpired(!user.getAccountNonExpired())
//                .roles(user.getRoles().stream().map(Role::getName).toArray(String[]::new))
                .authorities(authorities)
                .password(user.getPassword())
                .build();
    }
}
