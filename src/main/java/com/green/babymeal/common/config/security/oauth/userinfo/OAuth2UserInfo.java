package com.green.babymeal.common.config.security.oauth.userinfo;

import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId(); // 꼭 받아야 하는 부분이 될듯 싶음

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();
}
