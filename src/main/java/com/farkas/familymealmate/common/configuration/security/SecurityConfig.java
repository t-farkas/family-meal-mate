package com.farkas.familymealmate.common.configuration.security;

import com.farkas.familymealmate.security.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String REGISTER_ENDPOINT = "/api/auth/register";
    private static final String LOGIN_ENDPOINT = "/api/auth/login";
    private static final String API_DOCS = "/v3/api-docs/**";
    private static final String SWAGGER_UI = "/swagger-ui/**";
    private static final String SWAGGER_HTML = "/swagger-ui.html";
    private static final String SWAGGER_DOCS = "/documentation";
    private static final String SWAGGER_DOCS_CONFIG = "/documentation/swagger-config";
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(REGISTER_ENDPOINT).permitAll()
                        .requestMatchers(LOGIN_ENDPOINT).permitAll()
                        .requestMatchers(API_DOCS).permitAll()
                        .requestMatchers(SWAGGER_UI).permitAll()
                        .requestMatchers(SWAGGER_HTML).permitAll()
                        .requestMatchers(SWAGGER_DOCS_CONFIG).permitAll()
                        .requestMatchers(SWAGGER_DOCS).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
