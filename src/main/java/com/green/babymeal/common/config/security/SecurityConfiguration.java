package com.green.babymeal.common.config.security;

import com.green.babymeal.common.config.properties.CorsProperties;
import com.green.babymeal.common.config.security.filter.JwtAuthenticationFilter;
import com.green.babymeal.common.config.security.handler.*;
import com.green.babymeal.common.config.security.oauth.CustomOAuth2UserService;
import com.green.babymeal.common.config.security.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final CorsProperties corsProperties;
    private final CustomOAuth2UserService oAuth2UserService;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;

    //webSecurityCustomizer를 제외한 모든 것, 시큐리티를 거친다. 보안과 연관
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(authz ->
                            authz.requestMatchers(
                                            "/favicon.ico", "/js/**", "/img/**", "/css/**", "/static/**", "/", "/index.html"
                                            , "/swagger.html"
                                            , "/swagger-ui/**"
                                            , "/v3/api-docs/**"
                                            , "/*/oauth2/code/*"
                                            , "/oauth2/**"
                                            , "/oauth/**"
                                            , "/pic/**"
                                            , "/error"
                                            , "/err"
                                            ,"/**"  // 비회원 조회를 위해서
                                            , "/admin/**"
                                            , "/dm","/feed","/user/profile","/user/signin","/user/signup"
                                            ,"/api/v1/auth/**"
                                            ,"/api/v1/feed/**"
                                    ).permitAll()
                                 //   .requestMatchers(HttpMethod.GET, "/sign-api/refresh-token").permitAll()

                                    .requestMatchers("**exception**").permitAll()
                                    .anyRequest().authenticated()


                ) //사용 권한 체크

        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션 사용 X
        .httpBasic(http -> http.disable()) //UI 있는 시큐리티 설정을 비활성화
        .formLogin(http -> http.disable())

//                .oauth2Login(oauth2 -> oauth2.authorizationEndpoint(authorization  -> authorization.baseUri("/oauth2/authorization")
//                                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
//                        .redirectionEndpoint(redirection -> redirection.baseUri("/*/oauth2/code/*"))
//                        .userInfoEndpoint(userInfo  -> userInfo.userService(oAuth2UserService))
//                        .successHandler(oAuth2AuthenticationSuccessHandler())
//                        .failureHandler(oAuth2AuthenticationFailureHandler())
//                )
//        .formLogin(form ->  form.loginPage("/user/signin")
//                .usernameParameter("email")
//                .passwordParameter("pw")
//                .defaultSuccessUrl("/feed", true)
//                .loginProcessingUrl("/user/signin")
//
//        )
        .csrf(csrf -> csrf.disable()) //CSRF 보안이 필요 X, 쿠키와 세션을 이용해서 인증을 하고 있기 때문에 발생하는 일, https://kchanguk.tistory.com/197
        .exceptionHandling(except -> {
            except.accessDeniedHandler(tokenAccessDeniedHandler);
            except.authenticationEntryPoint(new RestAuthenticationEntryPoint());
        })
        .oauth2Login(oauth2 -> oauth2.loginPage("/user/signin")
                .authorizationEndpoint(authorization  -> authorization.baseUri("/oauth2/authorization")
                        .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository))
                .redirectionEndpoint(redirection -> redirection.baseUri("/*/oauth2/code/*"))
                .userInfoEndpoint(userInfo  -> userInfo.userService(oAuth2UserService))
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler())
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /*
     * Oauth 인증 실패 핸들러
     * */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository);
    }

    /*
     * Cors 설정
     * */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(corsConfig.getMaxAge());

        corsConfigSource.registerCorsConfiguration("/**", corsConfig);
        return corsConfigSource;
    }
}
