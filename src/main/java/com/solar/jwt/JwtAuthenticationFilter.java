package com.solar.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

    String path = request.getRequestURI();

    // 🚫 Bypass JWT filter for public paths like Swagger and Auth
    if (path.startsWith("/swagger-ui")
            || path.startsWith("/v3/api-docs")
            || path.equals("/swagger-ui.html")
            || path.startsWith("/api/v1/auth/login")
            || path.startsWith("/api/v1/users/login")
            || path.startsWith("/api/v1/users/register")
            || path.startsWith("/api/v1/users/verifyOtp")
            || path.startsWith("/api/v1/users/sendOtp")) {
        filterChain.doFilter(request, response);
        return;
    }

    String requestHeader = request.getHeader("Authorization");
    String username = null;
    String token = null;

    if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
        token = requestHeader.substring(7);
        try {
            username = jwtHelper.getUsernameFromToken(token);
        } catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException e) {
            e.printStackTrace();
        }
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Boolean validateToken = jwtHelper.validateToken(token, userDetails.getUsername());
        if (validateToken) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }

    filterChain.doFilter(request, response);
}

}
