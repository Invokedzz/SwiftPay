package org.swiftpay.services;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.LoginDTO;
import org.swiftpay.exceptions.AlreadyActiveException;
import org.swiftpay.exceptions.ForbiddenAccessException;
import org.swiftpay.exceptions.InvalidTokenException;
import org.swiftpay.exceptions.NonActiveUserException;
import org.swiftpay.model.User;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final TokenAuthService tokenAuthService;

    public String validateLoginPropertiesThenGenerateToken (LoginDTO loginDTO) {

        try {

            var searchForUser = new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password());

            var authentication = authenticationManager.authenticate(searchForUser);

            var token = tokenAuthService.generateLoginToken((User) authentication.getPrincipal());

            var signedJWT = SignedJWT.parse(token);

            if (signedJWT.getJWTClaimsSet().getClaim("Is Active").equals(Boolean.FALSE)) {

                throw new NonActiveUserException("This account is not active. Please, try to activate it!");

            }

            return token;

        } catch (ParseException ex) {

            throw new InvalidTokenException("Something went wrong while parsing the token. Please, try again.");

        }

    }

    public void compareIdFromTheSessionWithTheIdInTheUrl (HttpHeaders headers, Long sentId) {

        Long sessionId = tokenAuthService.findSessionId(headers);

        if (!sessionId.equals(sentId)) {

            throw new ForbiddenAccessException("You are not allowed to access this session");

        }

    }

    public void checkIfUserIsAlreadyActive (Boolean isActive) {

        if (isActive) {

            throw new AlreadyActiveException("This user is already active!");

        }

    }

}
