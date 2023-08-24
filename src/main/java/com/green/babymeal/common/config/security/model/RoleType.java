package com.green.babymeal.common.config.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum RoleType {
    USER("ROLE_USER", "일반 사용자 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한");

    private final String code;
    private final String displayName;

    public static Set<RoleType> of(List<String> roles) {
        Set<RoleType> roleTypes = new HashSet();
        return Arrays.stream(RoleType.values())
                .filter(r -> roles.stream().filter(item -> item.equals(r.code)).toList().size() > 0).collect(Collectors.toSet());
    }
}
