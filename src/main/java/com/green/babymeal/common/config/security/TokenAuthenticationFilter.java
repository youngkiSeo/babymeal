package com.green.babymeal.common.config.security;

import com.green.babymeal.common.config.properties.AppProperties;
import com.green.babymeal.common.config.redis.RedisService;
import com.green.babymeal.common.config.security.model.AuthToken;
import com.green.babymeal.common.utils.MyHeaderUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final MyHeaderUtils headerUtils;
    private final AuthTokenProvider tokenProvider;
    private final RedisService redisService;
    private final AppProperties appProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
            throws ServletException, IOException {
        String token = headerUtils.getAccessToken(req);
        log.info("JwtAuthenticationFilter - doFilterInternal: 토큰 추출 token: {}", token);

        log.info("JwtAuthenticationFilter - doFilterInternal: 토큰 유효성 체크 시작");
        if(token != null) {
            AuthToken authToken = tokenProvider.convertAuthToken(token, appProperties.getAccessTokenKey());
            if(authToken.validate()) {
                String redisBlackTokenKey = String.format("%s:%s", appProperties.getAuth().getRedisAccessBlackKey(), token);
                String isLogout = redisService.getValues(redisBlackTokenKey);
                if(ObjectUtils.isEmpty(isLogout)) {
                    Authentication authentication = tokenProvider.getAuthentication(authToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("JwtAuthenticationFilter - doFilterInternal: 토큰 유효성 체크 완료");
                }
            }
        }
        filterChain.doFilter(req, res);
    }
}
