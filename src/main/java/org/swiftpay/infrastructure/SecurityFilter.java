package org.swiftpay.infrastructure;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.swiftpay.model.User;
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

            var userDetails = userDetailsService.loadUserByUsername(subject);

            if (userDetails instanceof User user && !user.getActive()) {

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                response.getWriter().write("User account is deactivated.");

                return;

            }

            var auth = new UsernamePasswordAuthenticationToken(

                    userDetails,
                    null,
                    userDetails.getAuthorities()

            );

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
