package org.swiftpay.services;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.swiftpay.dtos.ReactivateUserDTO;
import org.swiftpay.dtos.RegisterDTO;
import org.swiftpay.exceptions.UserNotFoundException;
import org.swiftpay.model.DeleteRegister;
import org.swiftpay.model.User;
import org.swiftpay.model.Wallet;
import org.swiftpay.repositories.DeleteRegisterRepository;
import org.swiftpay.repositories.RoleRepository;
import org.swiftpay.repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;


@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserServices.class)
class UserServicesTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private CPFValidator cpfValidator;

    @MockitoBean
    private CNPJValidator cnpjValidator;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private DeleteRegisterRepository deleteRegisterRepository;

    @MockitoBean
    private RoleRepository roleRepository;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private TokenAuthService tokenAuthService;

    @Test
    void setupClientRegister_ThenSave () {

        RegisterDTO registerDTO = new RegisterDTO("Username", "username@gmail.com", "97925154888", "password");

        User user = new User(registerDTO);

        Wallet wallet = new Wallet();

        wallet.setBalance(BigDecimal.valueOf(500.0));

        user.setWallet(wallet);

        wallet.setUser(user);

        Mockito.when(userRepository.save(user)).thenReturn(user);

        Mockito.when(cpfValidator.isEligible(user.getCpfCnpj())).thenReturn(true);

        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn(Mockito.anyString());

        userRepository.save(user);

        cpfValidator.isEligible(user.getCpfCnpj());

        passwordEncoder.encode(user.getPassword());

        roleRepository.insertRole(Mockito.anyLong(), Mockito.anyLong());

        Assertions.assertThat(cpfValidator.isEligible(user.getCpfCnpj())).isTrue();

        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(user.getPassword());

        Mockito.verify(cpfValidator, Mockito.times(2)).isEligible(user.getCpfCnpj());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);

        Mockito.verify(roleRepository, Mockito.times(1)).insertRole(Mockito.anyLong(), Mockito.anyLong());

    }

    @Test
    void setupSellerRegister_ThenSave () {

        RegisterDTO registerDTO = new RegisterDTO("Username", "username@gmail.com", "75679584000172", "password");

        User user = new User(registerDTO);

        Wallet wallet = new Wallet();

        wallet.setBalance(BigDecimal.valueOf(500.0));

        user.setWallet(wallet);

        wallet.setUser(user);

        Mockito.when(userRepository.save(user)).thenReturn(user);

        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn(Mockito.anyString());

        Mockito.when(cnpjValidator.isEligible(user.getCpfCnpj())).thenReturn(true);

        userRepository.save(user);

        cnpjValidator.isEligible(user.getCpfCnpj());

        passwordEncoder.encode(user.getPassword());

        roleRepository.insertRole(Mockito.anyLong(), Mockito.anyLong());

        Mockito.verify(passwordEncoder, Mockito.times(1)).encode(user.getPassword());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);

        Mockito.verify(roleRepository, Mockito.times(1)).insertRole(Mockito.anyLong(), Mockito.anyLong());

    }

    @Test
    void login_ThenGenerateToken () {

        var mockAuthentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        String mockJwtToken = "mocked-jwt-token";

        Mockito.when(tokenAuthService.generateToken(new User())).thenReturn(mockJwtToken);

        tokenAuthService.generateToken(new User());

        Mockito.verify(tokenAuthService, Mockito.times(1)).generateToken(new User());

    }

    @Test
    void reactivateAccount_ThenReturn () {

        ReactivateUserDTO reactivateUserDTO = new ReactivateUserDTO("kys@gmail.com");

        Mockito.when(userRepository.findByEmail(reactivateUserDTO.email())).thenReturn(Optional.of(new User()));

        var foundUser = userRepository.findByEmail(reactivateUserDTO.email()).orElse(null);

        if (foundUser != null) {

            foundUser.activate();

            Assertions.assertThat(foundUser.getActive()).isTrue();

            Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(foundUser);

            deleteRegisterRepository.deleteByUser_Id(foundUser.getId());

            userRepository.save(foundUser);

            Mockito.verify(userRepository, Mockito.times(1)).save(foundUser);

            Mockito.verify(deleteRegisterRepository, Mockito.times(1)).deleteByUser_Id(foundUser.getId());

        }

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(reactivateUserDTO.email());

    }

    @Test
    void reactivateAccount_ThenThrowAnException () {

        ReactivateUserDTO reactivateUserDTO = new ReactivateUserDTO(null);

        Mockito.when(userRepository.findByEmail(reactivateUserDTO.email())).thenReturn(Optional.of(new User()));

        var foundUser = userRepository.findByEmail(reactivateUserDTO.email()).orElse(null);

        if (foundUser != null) {

            foundUser.activate();

            Assertions.assertThat(foundUser.getActive()).isTrue();

            Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(foundUser);

            deleteRegisterRepository.deleteByUser_Id(foundUser.getId());

            userRepository.save(foundUser);

            Mockito.verify(userRepository, Mockito.times(1)).save(foundUser);

            Mockito.verify(deleteRegisterRepository, Mockito.times(1)).deleteByUser_Id(foundUser.getId());

        }

        Mockito.doThrow(UserNotFoundException.class).when(userRepository).findByEmail(null);

        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(reactivateUserDTO.email());

    }

    @Test
    void disableAccount_ThenReturn () {

        Long id = 1L;

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(new User()));

        var foundUser = userRepository.findById(id).orElse(null);

        if (foundUser != null) {

            foundUser.deactivate();

            DeleteRegister deleteRegister = new DeleteRegister();

            deleteRegister.setUser(foundUser);

            deleteRegister.setDeleteDate(LocalDate.now());

            Mockito.when(deleteRegisterRepository.save(Mockito.any(DeleteRegister.class))).thenReturn(deleteRegister);

            Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(foundUser);

            deleteRegisterRepository.save(deleteRegister);

            userRepository.save(foundUser);

            Assertions.assertThat(foundUser.getActive()).isFalse();

            Mockito.verify(userRepository, Mockito.times(1)).save(foundUser);

            Mockito.verify(deleteRegisterRepository, Mockito.times(1)).save(deleteRegister);

        }

    }

    @Test
    void disableAccount_ThenThrowAnException () {

        Long id = 1L;

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        var foundUser = userRepository.findById(id).orElse(null);

        if (foundUser != null) {

            foundUser.deactivate();

            DeleteRegister deleteRegister = new DeleteRegister();

            deleteRegister.setUser(foundUser);

            deleteRegister.setDeleteDate(LocalDate.now());

            Mockito.when(deleteRegisterRepository.save(Mockito.any(DeleteRegister.class))).thenReturn(deleteRegister);

            Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(foundUser);

            deleteRegisterRepository.save(deleteRegister);

            userRepository.save(foundUser);

            Assertions.assertThat(foundUser.getActive()).isFalse();

            Mockito.verify(userRepository, Mockito.times(1)).save(foundUser);

            Mockito.verify(deleteRegisterRepository, Mockito.times(1)).save(deleteRegister);

        }

        Assertions.assertThatThrownBy(() -> {

            throw new UserNotFoundException("User not found");

        }).isInstanceOf(UserNotFoundException.class);

    }

}