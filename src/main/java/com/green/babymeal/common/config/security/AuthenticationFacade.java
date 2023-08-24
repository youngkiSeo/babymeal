package com.green.babymeal.common.config.security;

import com.green.babymeal.common.config.security.model.UserPrincipal;
import com.green.babymeal.common.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public UserEntity getLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
        return UserEntity.builder().iuser(userDetails.getIuser()).build();
    }
}
