package co.istad.identity.security;

import co.istad.identity.domain.Permission;
import co.istad.identity.domain.Role;
import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    private final String uuid;

    private final String username;

    private final String email;

    private final String password;

    private final String familyName;

    private final String givenName;

    private final String phoneNumber;

    private final String gender;

    private final LocalDate dob;

    private final String profileImage;

    private final String coverImage;

    private final Boolean accountNonExpired;

    private final Boolean accountNonLocked;

    private final Boolean credentialsNonExpired;

    private final Boolean isEnabled;
    
    private final Set<Role> roles;
    
    private final Set<Permission> permissions;

    public CustomUserDetails( String uuid, String username, String email, String password, String familyName, String givenName, String phoneNumber, String gender, LocalDate dob, String profileImage, String coverImage, Boolean accountNonExpired, Boolean accountNonLocked, Boolean credentialsNonExpired, Boolean isEnabled, Set<Role> roles, Set<Permission> permissions) {
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.familyName = familyName;
        this.givenName = givenName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.dob = dob;
        this.profileImage = profileImage;
        this.coverImage = coverImage;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.isEnabled = isEnabled;
        this.roles = roles;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
