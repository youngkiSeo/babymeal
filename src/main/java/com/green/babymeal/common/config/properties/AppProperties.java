package com.green.babymeal.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

@Getter
@ConfigurationProperties(prefix = "app") // yaml에 있는 것을 객체로 가져와서 사용을 하는 부분 이름만 맞춰주면 된다.
public class AppProperties {
    private final Auth auth = new Auth(); // yaml auth 셋팅을 가져오는 부분
    private final OAuth2 oauth2 = new OAuth2(); // yaml oauth2 셋팅을 가져오는 부분

    @Setter
    public Key accessTokenKey;

    @Setter
    private Key refreshTokenKey;

    @Getter
    @Setter
    public static class Auth {// yaml auth 셋팅을 가져오는 부분
        private String headerSchemeName;
        private String tokenType;
        private String aceessSecret;
        private long accessTokenExpiry;
        private String refreshSecret;
        private long refreshTokenExpiry;
        private String redisAccessBlackKey;
        private String redisRefreshKey;
    }

    @Getter
    @Setter
    public static final class OAuth2 {// yaml oauth2 셋팅을 가져오는 부분
        private List<String> authorizedRedirectUris = new ArrayList<>();
    }
}
