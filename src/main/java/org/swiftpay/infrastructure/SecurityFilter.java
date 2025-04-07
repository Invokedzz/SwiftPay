package org.swiftpay.infrastructure;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.swiftpay.services.TokenAuthService;
import org.swiftpay.services.UserDetailsServiceImpl;

import java.io.IOException;

@Component
@SuppressWarnings("NullableProblems")
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenAuthService tokenAuthService;

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityFilter (TokenAuthService tokenAuthService, UserDetailsServiceImpl userDetailsService) {

        this.tokenAuthService = tokenAuthService;

        this.userDetailsService = userDetailsService;

    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var token = filterToken(request);

        if (token != null) {

            var subject = tokenAuthService.verifyToken(token);

            var user = userDetailsService.loadUserByUsername(subject);

            var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);

        }

        filterChain.doFilter(request, response);

    }

    private String filterToken (HttpServletRequest request) {

        String tokenSession = request.getHeader("Authorization");

        if (tokenSession != null && tokenSession.startsWith("Bearer ")) {

            return tokenSession.replace("Bearer ", "");

        }

        return null;

    }

}
