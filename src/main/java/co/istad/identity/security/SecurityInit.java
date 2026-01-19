package co.istad.identity.security;

import co.istad.identity.domain.Role;
import co.istad.identity.domain.User;
import co.istad.identity.features.oauth2.JpaRegisteredClientRepository;
import co.istad.identity.features.role.RoleRepository;
import co.istad.identity.features.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
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
    private final JpaRegisteredClientRepository jpaRegisteredClientRepository;

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

    @PostConstruct
    public void initClient() {
        if (jpaRegisteredClientRepository.findByClientId("itp-standard") == null) {
            TokenSettings tokenSettings = TokenSettings.builder()
                    .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                    .accessTokenTimeToLive(Duration.ofDays(3))
                    .reuseRefreshTokens(false)
                    .refreshTokenTimeToLive(Duration.ofDays(5))
                    .build();

            ClientSettings clientSettings = ClientSettings.builder()
                    .requireProofKey(true)
                    .requireAuthorizationConsent(false)
                    .build();

            var client = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("itp-standard")
                    .clientName("ITP Standard")
                    .clientSecret(passwordEncoder.encode("secret"))
                    .clientSettings(clientSettings)
                    .tokenSettings(tokenSettings)
                    .authorizationGrantTypes(auth -> {
                        auth.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                        auth.add(AuthorizationGrantType.REFRESH_TOKEN);
                        auth.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                    })
                    .clientAuthenticationMethods(auth -> {
                        auth.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    })
                    .clientIdIssuedAt(Instant.now())
                    .postLogoutRedirectUri("http://localhost:9090")
                    .redirectUris(uri -> {
                                uri.add("http://localhost:9090/login/oauth2/code/itp-standard");
                            }
                    )
                    .scopes(scope -> {
                                scope.add(OidcScopes.OPENID);
                                scope.add(OidcScopes.EMAIL);
                                scope.add(OidcScopes.PROFILE);
                            }
                    )
                    .build();
            jpaRegisteredClientRepository.save(client);
        }
    }
}
