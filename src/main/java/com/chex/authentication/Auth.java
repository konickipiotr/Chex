package com.chex.authentication;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@NoArgsConstructor
public class Auth implements UserDetails {

    @GeneratedValue
    @Id
    private Long id;
    private String username;
    private String password;
    private String role;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime created;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastlogin;

    public Auth(String username, String password, String role, AccountStatus accountStatus) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.accountStatus = accountStatus;
        this.created = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return accountStatus.equals(AccountStatus.ACTIVE) || accountStatus.equals(AccountStatus.FIRSTLOGIN);
    }
}
