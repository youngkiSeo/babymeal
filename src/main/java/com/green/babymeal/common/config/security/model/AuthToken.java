package com.green.babymeal.common.config.security.model;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
public class AuthToken {
    public final static String IUSER = "iuser";
    public final static String ROLES = "roles";

    @Getter
    private final String token;
    private final Key key;

    public AuthToken(String id, long expiry, Key key) {
        this(id, expiry, key, null);
    }

    public AuthToken(String id, long expiry, Key key, LoginInfoVo vo) {
        this.key = key;
        this.token = createAuthToken(id, expiry, vo);
    }

    private String createAuthToken(String id, long expiry, LoginInfoVo vo) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(createClaims(id, vo))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiry))
                .signWith(key)
                .compact();
    }

    private Claims createClaims(String id, LoginInfoVo vo) {
        Claims claims = Jwts.claims();
        claims.setSubject(id);
        if(vo != null) {
            claims.put(IUSER, vo.getIuser());
            claims.put(ROLES, vo.getRoles());
        }
        return claims;
    }

    public boolean validate() {
        return this.getTokenClaims() != null;
    }

    public long getTokenExpirationTime() {
        return getTokenClaims().getExpiration().getTime();
    }

    public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

    public UserPrincipal getUserDetails() {
        Claims claims = getTokenClaims();
        String uid = claims.getSubject();
        Long iuser = claims.get(IUSER, Long.class);
        List<String> roles = (List<String>) claims.get(ROLES);
        return UserPrincipal.builder()
                .loginInfoVo(new LoginInfoVo(iuser, roles))
                .uid(uid)
                .build();
    }
}
