package co.istad.identity.security;

import co.istad.identity.domain.Role;
import co.istad.identity.domain.User;
import co.istad.identity.features.role.RoleRepository;
import co.istad.identity.features.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityInit {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        if(userRepository.count() == 0) {
            User user = new User();
            user.setUuid(UUID.randomUUID().toString());
            user.setUsername("kungsovannda");
            user.setPassword(passwordEncoder.encode("password"));
            user.setDob(LocalDate.of(2006, Month.NOVEMBER, 18));
            user.setEmail("kungsovannda@gmail.com");
            user.setCoverImage("placeholder.jpg");
            user.setGender("Male");
            user.setFamilyName("Kung");
            user.setGivenName("Sovannda");
            user.setIsEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setPhoneNumber("+85516797411");
            user.setProfileImage("placeholder.jpg");

            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ADMIN"));
            roles.add(roleRepository.findByName("USER"));
            user.setRoles(roles);

            userRepository.save(user);
        }
    }
}
