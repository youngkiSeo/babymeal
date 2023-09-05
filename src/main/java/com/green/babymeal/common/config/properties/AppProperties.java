package com.green.babymeal.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

@Getter
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();

    @Setter
    public Key accessTokenKey;

    @Setter
    private Key refreshTokenKey;

    @Getter
    @Setter
    public static class Auth {
        private String headerSchemeName;
        private String tokenType;
        private String accessSecret;
        private long accessTokenExpiry;
        private String refreshSecret;
        private long refreshTokenExpiry;
        private String redisAccessBlackKey;
        private String redisRefreshKey;
    }

    @Getter
    @Setter
    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();
    }
}
