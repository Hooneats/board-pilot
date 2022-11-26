package com.example.pilot2.dto.security;

import com.example.pilot2.dto.RoleDto;
import com.example.pilot2.dto.UserDto;
import com.example.pilot2.dto.constant.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal implements UserDetails, Serializable {
    String username;
    String password;
    Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal from(UserDto userDto) {
        Set<SimpleGrantedAuthority> authoritiesSet = userDto.getRoleDtos().stream()
                .map(RoleDto::getAuthority)
                .map(Authority::getRole)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());

        return new UserPrincipal(userDto.getUsername(), userDto.getPassword(), authoritiesSet);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return true;
    }
}
