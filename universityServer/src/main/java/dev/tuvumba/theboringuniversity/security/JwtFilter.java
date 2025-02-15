package dev.tuvumba.theboringuniversity.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();

        if (requestUri.startsWith("/auth/login") || requestUri.startsWith("/swagger-ui") || requestUri.startsWith("/v3")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("Missing or malformed Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractToken(authHeader);

        try {
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsername(token);
                List<String> roles = jwtUtil.extractRoles(token);

                logger.debug("JWT valid. Username:" + username + ", Roles: " + roles);

                List<GrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.debug("User " + username + " authenticated successfully");
            } else {
                logger.warn("JWT validation failed");
            }
        } catch (Exception e) {
            logger.error("Authentication failed: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid or expired token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(String authHeader) throws IOException {
        String token = authHeader.substring(7);
        if (token.startsWith("{") && token.endsWith("}")) {
            JsonNode node = objectMapper.readTree(token);
            return node.get("token").asText();
        }
        return token;
    }
}