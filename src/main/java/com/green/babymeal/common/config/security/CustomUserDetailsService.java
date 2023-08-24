package com.green.babymeal.common.config.security;

import com.green.babymeal.common.config.security.model.ProviderType;
import com.green.babymeal.common.config.security.model.UserPrincipal;
import com.green.babymeal.common.entity.UserEntity;
import com.green.babymeal.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository rep;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = rep.findByProviderTypeAndEmail(ProviderType.LOCAL, username);
        if (user == null) {
            throw new UsernameNotFoundException("Can not find username.");
        }
        return UserPrincipal.create(user);
    }
}
