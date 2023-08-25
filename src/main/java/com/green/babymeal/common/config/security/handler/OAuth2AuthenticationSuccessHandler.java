package com.green.babymeal.common.config.security.handler;

import com.green.babymeal.common.config.properties.AppProperties;
import com.green.babymeal.common.config.redis.RedisService;
import com.green.babymeal.common.config.security.AuthTokenProvider;
import com.green.babymeal.common.config.security.model.*;
import com.green.babymeal.common.config.security.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.green.babymeal.common.config.security.oauth.userinfo.OAuth2UserInfo;
import com.green.babymeal.common.config.security.oauth.userinfo.OAuth2UserInfoFactory;
import com.green.babymeal.common.utils.MyHeaderUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.green.babymeal.common.config.security.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final RedisService redisService;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final AuthTokenProvider authTokenProvider;
    private final AppProperties appProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        log.info("targetUrl : {}", targetUrl);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = MyHeaderUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new IllegalArgumentException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }
        //TODO: getDefaultTargetUrl() 어떤 값이 넘어오는지 체크
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        ProviderType providerType = ProviderType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        UserPrincipal user = ((UserPrincipal) authentication.getPrincipal());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());

        List<String> roles = new ArrayList();
        roles.add(RoleType.USER.getCode());
        String ip = request.getRemoteAddr();
        log.info("oauth-login ip : {}", ip);

        // RT가 이미 있을 경우
        String redisRefreshTokenKey = String.format("RT(%s):%s", providerType.name(), user.getIuser());
        if(redisService.getValues(redisRefreshTokenKey) != null) {
            redisService.deleteValues(redisRefreshTokenKey); // 삭제
        }
        LoginInfoVo vo = new LoginInfoVo(user.getIuser(), roles);

        AuthToken at = authTokenProvider.createAccessToken(userInfo.getId(), vo);
        AuthToken rt = authTokenProvider.createRefreshToken(userInfo.getId(), vo);

        redisService.setValuesWithTimeout(redisRefreshTokenKey, rt.getToken(), appProperties.getAuth().getRefreshTokenExpiry());
        int cookieMaxAge = (int) appProperties.getAuth().getRefreshTokenExpiry() / 1000;

        MyHeaderUtils.deleteCookie(request, response, REFRESH_TOKEN);
        MyHeaderUtils.addCookie(response, REFRESH_TOKEN, rt.getToken(), cookieMaxAge);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token", at.getToken())
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private List<String> getAuthority(Collection<? extends GrantedAuthority> authorities) {
        List<String> roles = new ArrayList();

        if (authorities != null) {
            for (GrantedAuthority grantedAuthority : authorities) {
                roles.add(grantedAuthority.getAuthority());
            }
        }
        return roles;
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
