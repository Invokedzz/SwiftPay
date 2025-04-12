package org.swiftpay.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.swiftpay.dtos.LoginDTO;
import org.swiftpay.dtos.ReactivateUserDTO;
import org.swiftpay.dtos.RegisterDTO;
import org.swiftpay.exceptions.UsernameNotFoundException;
import org.swiftpay.model.DeleteRegister;
import org.swiftpay.model.User;
import org.swiftpay.repositories.DeleteRegisterRepository;
import org.swiftpay.repositories.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserServices.class)
class UserServicesTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private DeleteRegisterRepository deleteRegisterRepository;

    @MockitoBean
    private RolesService rolesService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private MailService mailService;

    @Test
    void setupClientRegister_ThenSave () {

        RegisterDTO registerDTO = new RegisterDTO("Shang", "shang@gmail.com", "2938484729", "123456");

        User mockUser = new User();

        Mockito.when(rolesService.validateClientPropertiesBeforeRegister(registerDTO))
                .thenReturn(mockUser);

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(mockUser);

        User user = rolesService.validateClientPropertiesBeforeRegister(registerDTO);
        userRepository.save(user);
        rolesService.setupUserRolesAndSave(user);
        mailService.setupConfirmationEmailLogic(user);

        Mockito.verify(rolesService, Mockito.times(1)).validateClientPropertiesBeforeRegister(registerDTO);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(mailService, Mockito.times(1)).setupConfirmationEmailLogic(Mockito.any(User.class));

    }

    @Test
    void setupSellerRegister_ThenSave () {

        RegisterDTO registerDTO = new RegisterDTO("Shang", "shang@gmail.com", "2938484729", "123456");

        User mockUser = new User();

        Mockito.when(rolesService.validateSellerPropertiesBeforeRegister(registerDTO))
                .thenReturn(mockUser);

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(mockUser);

        User user = rolesService.validateSellerPropertiesBeforeRegister(registerDTO);
        userRepository.save(user);
        rolesService.setupUserRolesAndSave(user);
        mailService.setupConfirmationEmailLogic(user);

        Mockito.verify(rolesService, Mockito.times(1)).validateSellerPropertiesBeforeRegister(registerDTO);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(mailService, Mockito.times(1)).setupConfirmationEmailLogic(Mockito.any(User.class));

    }

    @Test
    void login_ThenGenerateToken () {

        LoginDTO loginDTO = new LoginDTO("Mr. M", "mrM@gmail.com");

        Mockito.when(authService.validateLoginPropertiesThenGenerateToken(loginDTO)).thenReturn(Mockito.anyString());

        authService.validateLoginPropertiesThenGenerateToken(loginDTO);

        Mockito.verify(authService, Mockito.times(1)).validateLoginPropertiesThenGenerateToken(loginDTO);

    }

    @Test
    void getProfileById_ThenReturnUser () {

        Long userId = 1L;
        User fakeUser = new User();
        HttpHeaders headers = new HttpHeaders();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(fakeUser));

        Mockito.doNothing().when(authService).compareIdFromTheSessionWithTheIdInTheUrl(headers, userId);

        userRepository.findById(userId);

        authService.compareIdFromTheSessionWithTheIdInTheUrl(headers, userId);

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);

        Mockito.verify(authService, Mockito.times(1)).compareIdFromTheSessionWithTheIdInTheUrl(headers, userId);

    }

    @Test
    void reactivateAccount_ThenReturn () {

        ReactivateUserDTO reactivateUserDTO = new ReactivateUserDTO("Apollo@gmail.com");

        Mockito.when(userRepository.findByEmail(reactivateUserDTO.email())).thenReturn(Optional.of(new User()));

        var queriedUser = userRepository.findByEmail(reactivateUserDTO.email()).orElse(null);

        if (queriedUser != null) {

            Mockito.when(userRepository.save(queriedUser)).thenReturn(queriedUser);

            queriedUser.activate();

            userRepository.save(queriedUser);

            deleteRegisterRepository.deleteByUser_Id(queriedUser.getId());

            Assertions.assertThat(queriedUser.getActive()).isTrue();

            Mockito.verify(userRepository, Mockito.times(1)).save(queriedUser);

            Mockito.verify(deleteRegisterRepository, Mockito.times(1)).deleteByUser_Id(queriedUser.getId());

            return;

        }

        Mockito.doThrow(UsernameNotFoundException.class);

    }

    @Test
    void disableAccountById_ThenReturn () {

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new User()));

        var user = userRepository.findById(Mockito.anyLong()).orElse(null);

        if (user != null) {

            Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

            userRepository.save(user);

            DeleteRegister deleteRegister = new DeleteRegister();

            deleteRegister.setUser(user);

            deleteRegister.setDeleteDate(LocalDate.now());

            deleteRegisterRepository.save(deleteRegister);

            mailService.setupDeletionEmailLogic(user.getEmail());

            Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));

            Mockito.verify(deleteRegisterRepository, Mockito.times(1)).save(Mockito.any(DeleteRegister.class));

            return;

        }

        Mockito.doThrow(UsernameNotFoundException.class);

    }

}