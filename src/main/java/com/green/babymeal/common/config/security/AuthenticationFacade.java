package com.green.babymeal.common.config.security;

import com.green.babymeal.common.config.security.model.UserPrincipal;
import com.green.babymeal.common.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public boolean isLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null;
    }
    public UserEntity getLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ("anonymousUser".equals(auth.getPrincipal())){
            return null;
        }
        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
        return UserEntity.builder().iuser(userDetails.getIuser()).build();
    }
}
