package com.green.babymeal.auth;

import com.green.babymeal.auth.model.AuthResVo;
import com.green.babymeal.auth.model.SignInReqDto;
import com.green.babymeal.auth.model.SignUpReqDto;
import com.green.babymeal.common.config.exception.AuthErrorCode;
import com.green.babymeal.common.config.exception.RestApiException;
import com.green.babymeal.common.config.properties.AppProperties;
import com.green.babymeal.common.config.redis.RedisService;
import com.green.babymeal.common.config.security.AuthTokenProvider;
import com.green.babymeal.common.config.security.model.*;
import com.green.babymeal.common.entity.UserEntity;
import com.green.babymeal.common.utils.MyHeaderUtils;
import com.green.babymeal.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final RedisService redisService;
    private final UserRepository userRep;
    private final AuthTokenProvider authTokenProvider;
    private final AppProperties appProperties;
    private final PasswordEncoder passwordEncoder;

    public AuthResVo signUp(SignUpReqDto dto
                        , HttpServletRequest req
                        , HttpServletResponse res) {

        String ip = req.getRemoteAddr();
        log.info("local-login ip : {}", ip);

        UserEntity p = UserEntity.builder()
                .uid(dto.getUid())
                .password(passwordEncoder.encode(dto.getUpw()))
                .name(dto.getUnm())
                .email(dto.getEmail())
                .providerType(ProviderType.LOCAL)
                .roleType(RoleType.USER)
                .address()
                .addressDetail()
                .
                .build();
        userRep.save(p);
        return processAuth(p, req, res);
    }

    public AuthResVo signIn(SignInReqDto dto, HttpServletRequest req, HttpServletResponse res) {
        String ip = req.getRemoteAddr();
        log.info("local-login ip : {}", ip);

        UserEntity r = userRep.findByProviderTypeAndUid(ProviderType.LOCAL, dto.getUid());
        if(r == null) {
            throw new RestApiException(AuthErrorCode.NOT_FOUND_ID);
        }

        if(!passwordEncoder.matches(dto.getUpw(), r.getPassword())) {
            throw new RestApiException(AuthErrorCode.VALID_PW);
        }

        // RT가 이미 있을 경우
        String redisRefreshTokenKey = String.format("%s:%s", appProperties.getAuth().getRedisRefreshKey(), r.getIuser());
        if(redisService.getValues(redisRefreshTokenKey) != null) {
            redisService.deleteValues(redisRefreshTokenKey); // 삭제
        }

        return processAuth(r, req, res);
    }

    private AuthResVo processAuth(UserEntity userEntity, HttpServletRequest req, HttpServletResponse res) {
        LoginInfoVo vo = new LoginInfoVo(userEntity.getIuser(), new ArrayList(Arrays.asList(userEntity.getRoleType().getCode())));

        AuthToken at = authTokenProvider.createAccessToken(userEntity.getUid(), vo);
        AuthToken rt = authTokenProvider.createRefreshToken(userEntity.getUid(), vo);

        String redisRefreshTokenKey = String.format("%s:%s", appProperties.getAuth().getRedisRefreshKey(), userEntity.getIuser());
        redisService.setValuesWithTimeout(redisRefreshTokenKey, rt.getToken(), appProperties.getAuth().getRefreshTokenExpiry());

        int cookieMaxAge = (int) (appProperties.getAuth().getRefreshTokenExpiry() * 0.001);
        MyHeaderUtils.deleteCookie(req, res, REFRESH_TOKEN);
        MyHeaderUtils.addCookie(res, REFRESH_TOKEN, rt.getToken(), cookieMaxAge);

        return AuthResVo.builder().accessToken(at.getToken()).build();
    }

    public void signOut(String accessToken
            , HttpServletRequest req
            , HttpServletResponse res) {

        if(accessToken != null) {
            AuthToken authToken = new AuthToken(accessToken, appProperties.getAccessTokenKey());

            String blackAccessTokenKey = String.format("%s:%s", appProperties.getAuth().getRedisAccessBlackKey(), accessToken);
            long expiration = authToken.getTokenExpirationTime() - LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            if(expiration > 0) {
                redisService.setValuesWithTimeout(blackAccessTokenKey, "logout", expiration);
            }
        }

        //cookie에서 값 가져오기
        Optional<Cookie> refreshTokenCookie = MyHeaderUtils.getCookie(req, REFRESH_TOKEN);
        if(refreshTokenCookie.isEmpty()) {
            throw new RestApiException(AuthErrorCode.NOT_FOUND_REFRESH_TOKEN);
        }

        Optional<String> refreshToken = refreshTokenCookie.map(Cookie::getValue);
        if(refreshToken.isPresent()) {
            AuthToken authToken = new AuthToken(refreshToken.get(), appProperties.getRefreshTokenKey());
            long iuser = authToken.getUserDetails().getIuser();
            String redisRefreshTokenKey = String.format("%s:%s", appProperties.getAuth().getRedisRefreshKey(), iuser);
            redisService.deleteValues(redisRefreshTokenKey);
        }
        MyHeaderUtils.deleteCookie(req, res, REFRESH_TOKEN);
    }

    public AuthResVo refresh(HttpServletRequest req) {
        Optional<Cookie> refreshTokenCookie = MyHeaderUtils.getCookie(req, REFRESH_TOKEN);
        if(refreshTokenCookie.isEmpty()) {
            throw new RestApiException(AuthErrorCode.NOT_FOUND_REFRESH_TOKEN);
        }

        String refreshToken = refreshTokenCookie.map(Cookie::getValue).get();

        AuthToken authToken = new AuthToken(refreshToken, appProperties.getRefreshTokenKey());
        UserPrincipal up = authToken.getUserDetails();
        LoginInfoVo vo = new LoginInfoVo(up.getIuser(), up.getRoles());

        AuthToken at = authTokenProvider.createAccessToken(up.getUid(), vo);

        return AuthResVo.builder().accessToken(at.getToken()).build();
    }
}
