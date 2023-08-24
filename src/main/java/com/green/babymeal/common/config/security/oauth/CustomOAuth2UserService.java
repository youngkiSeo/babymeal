package com.green.babymeal.common.config.security.oauth;

import com.green.babymeal.common.config.exception.OAuthProviderMissMatchException;
import com.green.babymeal.common.config.security.model.ProviderType;
import com.green.babymeal.common.config.security.model.RoleType;
import com.green.babymeal.common.config.security.model.UserPrincipal;
import com.green.babymeal.common.config.security.oauth.userinfo.OAuth2UserInfo;
import com.green.babymeal.common.config.security.oauth.userinfo.OAuth2UserInfoFactory;
import com.green.babymeal.common.entity.UserEntity;
import com.green.babymeal.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository rep;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        UserEntity savedUser = rep.findByProviderTypeAndIuser(providerType, userInfo.getEmail());

        if (savedUser != null) {
            if (providerType != savedUser.getProviderType()) {
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + providerType +
                                " account. Please use your " + savedUser.getProviderType() + " account to login."
                );
            }
            updateUser(savedUser, userInfo);
        } else {
            savedUser = createUser(userInfo, providerType);
        }

        return UserPrincipal.create(savedUser, user.getAttributes());
    }

    private UserEntity createUser(OAuth2UserInfo userInfo, ProviderType providerType) {

        return rep.saveAndFlush(UserEntity.builder()
                        .providerType(providerType)
                        .name(userInfo.getName())
                        .roleType(RoleType.USER)
                        .email(userInfo.getEmail())
                        .image(userInfo.getImageUrl())
                .build());
    }

    private UserEntity updateUser(UserEntity user, OAuth2UserInfo userInfo) {
        if (userInfo.getName() != null && !user.getName().equals(userInfo.getName())) {
            user.setName(userInfo.getName());
        }
        if (userInfo.getImageUrl() != null && !user.getImage().equals(userInfo.getImageUrl())) {
            user.setImage(userInfo.getImageUrl());
        }
        return user;
    }
}
