package com.green.babymeal.common.config.security.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LoginInfoVo {
    private final Long iuser;
    private final List<String> roles;

    public LoginInfoVo(Long iuser) {
        this(iuser, null);
    }

    public LoginInfoVo(Long iuser, List<String> roles) {
        this.iuser = iuser;
        this.roles = roles != null ? roles : new ArrayList();
    }

    public void addRole(String role) {
        this.roles.add(role.toUpperCase());
    }
}
