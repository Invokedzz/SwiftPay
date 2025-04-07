package org.swiftpay.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.swiftpay.exceptions.InvalidTokenException;
import org.swiftpay.exceptions.TokenCreationException;
import org.swiftpay.model.User;

import java.time.Instant;
import java.util.List;

@Service
public class TokenAuthService {

    @Value("spring.security.oauth2.client.registration")
    private String secret;

    public String generateToken (User user) {

        List <String> userRoles = user.getAuthorities()
                                      .stream()
                                      .map(GrantedAuthority::getAuthority)
                                      .toList();

        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                      .withIssuer("SwiftPayments")
                      .withSubject(user.getUsername())
                      .withClaim("User Id", user.getId())
                      .withClaim("Roles", userRoles)
                      .withClaim("Is Active", user.getActive())
                      .withExpiresAt(tokenExpirationInstant())
                      .sign(algorithm);

        } catch (JWTCreationException ex) {

            throw new TokenCreationException(ex.getMessage());

        }

    }

    public String verifyToken (String token) {

        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("SwiftPayments")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException ex) {

            throw new InvalidTokenException(ex.getMessage());

        }

    }

    private Instant tokenExpirationInstant () {

        return Instant.now().plusSeconds(3600);

    }

}
