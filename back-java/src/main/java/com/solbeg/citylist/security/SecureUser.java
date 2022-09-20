package com.solbeg.citylist.security;


import com.solbeg.citylist.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecureUser implements UserDetails {
    private final List<SimpleGrantedAuthority> userRoles;
    private final String password;
    private final String username;

    public SecureUser(UserDTO userDTO) {
        this.userRoles = new ArrayList<>();
        userDTO.getUserRoles().forEach(userRoleDTO -> this.userRoles.add(new SimpleGrantedAuthority(userRoleDTO.getRole())));

        this.password = userDTO.getPassword();
        this.username = userDTO.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userRoles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
