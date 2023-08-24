package com.green.babymeal.common.config.security;

import com.green.babymeal.common.config.exception.TokenValidFailedException;
import com.green.babymeal.common.config.properties.AppProperties;
import com.green.babymeal.common.config.security.model.AuthToken;
import com.green.babymeal.common.config.security.model.LoginInfoVo;
import com.green.babymeal.common.config.security.model.UserPrincipal;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
@Component
public class AuthTokenProvider {
    private final AppProperties APP_PROPERTIES;

    @Autowired
    public AuthTokenProvider(AppProperties appProperties) {
        this.APP_PROPERTIES = appProperties;

        byte[] accessKeyBytes = Decoders.BASE64.decode(appProperties.getAuth().getAceessSecret());
        this.APP_PROPERTIES.setAccessTokenKey(Keys.hmacShaKeyFor(accessKeyBytes));

        byte[] refreshKeyBytes = Decoders.BASE64.decode(appProperties.getAuth().getRefreshSecret());
        this.APP_PROPERTIES.setRefreshTokenKey(Keys.hmacShaKeyFor(refreshKeyBytes));
    }

    public AuthToken createAccessToken(String id) {
        return createAccessToken(id, null);
    }

    public AuthToken createAccessToken(String id, LoginInfoVo vo) {
        long expiry = APP_PROPERTIES.getAuth().getAccessTokenExpiry();
        Key key = APP_PROPERTIES.getAccessTokenKey();
        return createToken(id, expiry, key, vo);
    }

    public AuthToken createRefreshToken(String id) {
        return createRefreshToken(id, null);
    }

    public AuthToken createRefreshToken(String id, LoginInfoVo vo) {
        long expiry = APP_PROPERTIES.getAuth().getRefreshTokenExpiry();
        Key key = APP_PROPERTIES.getRefreshTokenKey();
        return createToken(id, expiry, key, vo);
    }

    private AuthToken createToken(String id, long expiry, Key key, LoginInfoVo vo) {
        return vo == null ? new AuthToken(id, expiry, key) : new AuthToken(id, expiry, key, vo);
    }

    public AuthToken convertAuthToken(String token, Key key) {
        return new AuthToken(token, key);
    }

    public Authentication getAuthentication(AuthToken authToken) {
        if(authToken.validate()) {
            UserPrincipal userPrincipal = authToken.getUserDetails();
            return new UsernamePasswordAuthenticationToken(userPrincipal, authToken, userPrincipal.getAuthorities());
        } else {
            throw new TokenValidFailedException();
        }
    }
}
