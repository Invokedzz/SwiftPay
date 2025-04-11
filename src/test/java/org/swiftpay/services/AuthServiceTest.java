package org.swiftpay.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TokenAuthService.class)
class AuthServiceTest {

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenAuthService tokenAuthService;

    @Test
    void authenticate() {}

}