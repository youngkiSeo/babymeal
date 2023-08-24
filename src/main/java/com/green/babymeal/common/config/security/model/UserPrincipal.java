package com.green.babymeal.common.config.security.model;

import com.green.babymeal.common.entity.UserEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
public class UserPrincipal implements UserDetails, OAuth2User {
    private LoginInfoVo loginInfoVo;
    private String uid;
    private String upw;
    private String unm;
    private Map<String, Object> attributes;

    public Long getIuser() {
        return loginInfoVo.getIuser();
    }
    public List<String> getRoles() { return loginInfoVo.getRoles(); }

    @Override
    public String getName() {
        return uid;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.loginInfoVo.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return upw;
    }

    @Override
    public String getUsername() {
        return unm;
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

    public static UserPrincipal create(UserEntity user) {
        LoginInfoVo loginInfoVo = new LoginInfoVo(user.getIuser());
        loginInfoVo.addRole(user.getRoleType().getCode());
        return UserPrincipal.builder()
                .loginInfoVo(loginInfoVo)
                .uid(user.getEmail())
                .unm(user.getName())
                .build();
    }

    public static UserPrincipal create(UserEntity user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = create(user);
        userPrincipal.setAttributes(attributes);

        return userPrincipal;
    }
}
