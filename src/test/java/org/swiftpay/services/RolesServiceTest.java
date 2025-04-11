package org.swiftpay.services;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.swiftpay.configuration.StellaConfig;
import org.swiftpay.repositories.RoleRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {RolesService.class, StellaConfig.class})
class RolesServiceTest {

    @MockitoBean
    private RoleRepository roleRepository;

    @Autowired
    private CPFValidator cpfValidator;

    @Autowired
    private CNPJValidator cnpjValidator;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    void validateUserPassword_ThenReturnTrue () {

        String rawPassword = "randomPassword";

        String encodedPassword = "$2a$10$K9vvJJsd.V2Qg5j1lt5XxeJrxC9qbVpgk.4eI3lx7E2gD9TGGby7S";

        Mockito.when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean isPasswordMatch = passwordEncoder.matches(rawPassword, encodedPassword);

        assertTrue(isPasswordMatch);

    }

    @Test
    void validateUserPassword_ThenReturnFalse () {

        String rawPassword = "randomPassword";

        String encodedPassword = "$2a$10$K9vvJJsd.V2Qg5j1lt5XxeJrxC9qbVpgk.4eI3lx7E2gD9TGGby7S";

        Mockito.when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        boolean isPasswordMatch = passwordEncoder.matches(rawPassword, encodedPassword);

        assertFalse(isPasswordMatch);

    }

    @Test
    void validateClientCPF_ThenReturnTrue () {

        String clientCPF = "88362263024";

        assertTrue(cpfValidator.isEligible(clientCPF));

    }

    @Test
    void validateClientCNPJ_ThenReturnTrue () {

        String sellerCNPJ = "40726175000103";

        assertTrue(cnpjValidator.isEligible(sellerCNPJ));

    }

    @Test
    void validateClientCPF_ThenReturnFalse () {

        String clientCPF = "9384725273";

        assertFalse(cpfValidator.isEligible(clientCPF));

        assertThrows(InvalidStateException.class, () -> cpfValidator.assertValid(clientCPF));

    }

    @Test
    void validateClientCNPJ_ThenReturnFalse () {

        String sellerCNPJ = "183928346218";

        assertFalse(cnpjValidator.isEligible(sellerCNPJ));

        assertThrows(InvalidStateException.class, () -> cnpjValidator.assertValid(sellerCNPJ));

    }

    @Test
    void insertRolesToUser_ThenSave () {

        roleRepository.insertRole(Mockito.anyLong(), Mockito.anyLong());

        Mockito.verify(roleRepository, Mockito.times(1)).insertRole(Mockito.anyLong(), Mockito.anyLong());

    }

    @Test
    void validateSellerProperties_ThenReturnUser () {

        roleRepository.insertRole(Mockito.anyLong(), Mockito.anyLong());

        Mockito.verify(roleRepository, Mockito.times(1)).insertRole(Mockito.anyLong(), Mockito.anyLong());

    }

}
