package com.green.babymeal.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {       // yaml cors 셋팅을 가져오는 부분
    private String allowedOrigins; // 케밥케이스 기법을 사용하지 못하는 이유는 자바에서는 _, $ 만 사용가능하기 때문이다.
    private String allowedMethods;
    private String allowedHeaders;
    private Long maxAge;
}
