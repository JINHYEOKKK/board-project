package com.springboot.config;

import com.springboot.auth.filter.JwtAuthenticationFilter;
import com.springboot.auth.jwt.JwtTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {
    private final JwtTokenizer jwtTokenizer;

    public SecurityConfiguration(JwtTokenizer jwtTokenizer) {
        this.jwtTokenizer = jwtTokenizer;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable() // Cross-Site Request Forgery
                .formLogin().disable()
                .cors(withDefaults()) // CORS 설정이 이와 같을 경우, corsConfigurationSource라는 이름으로 등록된 Bean을 이용
                .httpBasic().disable()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // 이메서드는 CORS 설정을 담당하는 메서드. 이 메서드로 다른 도메인에서 오는 HTTP 요청을 어떻게 처리할지 설정할 수 있다.
    // Cross-Origin-Resource-Sharing
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("POST, PATCH, GET, DELETE"));
        // setAllowedOrigins: 내장된 메서드로 허용할 출처를 설정. ("*") <-- 모든 출처를 허용. 어느 도메인이든 ok.
        // setAllowedMethods: 내장된 메서드로 허용할 HTTP 메서드. 위에 설정한 메서드들은 다른 도메인의 요청을 허용하겠다는 뜻.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("\\/**", configuration);
        /**
         * UrlBasedCorsConfigurationSource 객체를 생성.
         * 이 객체는 특정 URL 패턴에 CORS 설정을 적용하는 역할.
         * registerCorsConfiguration("/**", configuration)는
         * 모든 경로(/**)에 대해 앞서 정의한 CORS 설정(configuration)을 적용한다는 의미.
         */
        return source;
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager =
                    builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter =
                    new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);
            jwtAuthenticationFilter.setFilterProcessesUrl("/v1/auth/login");

            builder.addFilter(jwtAuthenticationFilter);
        }
    }
}


/**
 * CORS는 출처가 다른 스크립트 기반 HTTP 통신을 하더라도 선택적으로 리소스에 접근할 수 있는 권한을 부여하도록 브라우저에 알려주는 정책
 *
 * Cross-Origin-Resource-Sharing
 *
 * Cross-Site Request Forgery
 */