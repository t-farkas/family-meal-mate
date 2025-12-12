package com.farkas.familymealmate.security.jwt;

import com.farkas.familymealmate.security.CustomUserDetailService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Set<String> PUBLIC_ENDPOINTS = Set.of(
            "/api/auth/register",
            "/api/auth/login",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/swagger-ui/**",
            "/documentation",
            "/documentation/swagger-config",
            "/v3/api-docs/**"
    );
    private final CustomUserDetailService customUserDetailService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = extractToken(request);
            if (token != null) {
                String email = jwtService.extractEmail(token);
                if (unauthenticated(email)) {
                    authenticate(request, response, email, token);
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing or invalid");
                return;
            }
            filterChain.doFilter(request, response);

        } catch (JwtException | UsernameNotFoundException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: " + e.getMessage());
        }
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();
        return PUBLIC_ENDPOINTS.stream().anyMatch(p -> matcher.match(p, path));
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return authHeaderPresent(authHeader) ? authHeader.substring(7) : null;
    }

    private boolean authHeaderPresent(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    private boolean unauthenticated(String email) {
        return email != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void authenticate(HttpServletRequest request, HttpServletResponse response, String email, String token) throws IOException {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(email);

        if (jwtService.isTokenValid(token, userDetails.getUsername())) {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
        }
    }
}
