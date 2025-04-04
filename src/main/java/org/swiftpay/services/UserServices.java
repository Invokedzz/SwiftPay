package org.swiftpay.services;

import org.springframework.stereotype.Service;
import org.swiftpay.dtos.RegisterUserDTO;
import org.swiftpay.dtos.ViewAllUsersDTO;
import org.swiftpay.model.User;
import org.swiftpay.model.Wallet;
import org.swiftpay.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServices {

    private final UserRepository userRepository;

    public UserServices (UserRepository userRepository) {

        this.userRepository = userRepository;

    }

    public void register (RegisterUserDTO registerUserDTO) {

        User user = new User(registerUserDTO);

        Wallet wallet = new Wallet();

        wallet.setBalance(BigDecimal.valueOf(500.0));

        wallet.setUser(user);

        user.setWallet(wallet);

        userRepository.save(user);

    }

    public Set <ViewAllUsersDTO> getUsers () {

        return userRepository.findAllByActive(true).stream()
                .map(ViewAllUsersDTO::new)
                .collect(Collectors.toSet());

    }

    public void deleteAll () {

        userRepository.deleteAll();

    }

}
