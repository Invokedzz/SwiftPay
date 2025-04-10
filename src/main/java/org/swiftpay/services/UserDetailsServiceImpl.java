package org.swiftpay.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.swiftpay.exceptions.ForbiddenAccessException;
import org.swiftpay.model.User;
import org.swiftpay.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl (UserRepository userRepository) {

        this.userRepository = userRepository;

    }

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {

        var user = userRepository.findByUsername(username);

        if (user != null) {

            return user;

        }

        throw new UsernameNotFoundException("We weren't able to find a user with username: " + username);

    }

}
