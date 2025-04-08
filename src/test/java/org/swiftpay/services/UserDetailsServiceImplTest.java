package org.swiftpay.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.swiftpay.exceptions.UserNotFoundException;
import org.swiftpay.model.User;
import org.swiftpay.repositories.UserRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserDetailsServiceImpl.class)
class UserDetailsServiceImplTest {

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void searchForUser_ThenReturnIt () {

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Mockito.mock(User.class));

        userRepository.findByUsername(Mockito.anyString());

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());

    }

    @Test
    void searchForInvalidUser_ThenThrowAnException () {

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);

        Mockito.doThrow(UserNotFoundException.class).when(userRepository).findByUsername(null);

        Assertions.assertThrows(UserNotFoundException.class, () -> userRepository.findByUsername(null));

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(null);

    }

}