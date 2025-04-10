package org.swiftpay.services;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.RegisterDTO;
import org.swiftpay.exceptions.InvalidCNPJException;
import org.swiftpay.exceptions.InvalidCPFException;
import org.swiftpay.model.User;
import org.swiftpay.model.Wallet;
import org.swiftpay.repositories.RoleRepository;

import java.math.BigDecimal;

@Service
public class RolesService {

    private final RoleRepository roleRepository;

    private final CPFValidator cpfValidator;

    private final CNPJValidator cnpjValidator;

    private final PasswordEncoder passwordEncoder;

    public RolesService (RoleRepository roleRepository, CPFValidator cpfValidator, CNPJValidator cnpjValidator, PasswordEncoder passwordEncoder) {

        this.roleRepository = roleRepository;

        this.cpfValidator = cpfValidator;

        this.cnpjValidator = cnpjValidator;

        this.passwordEncoder = passwordEncoder;

    }

    public User validateClientPropertiesBeforeRegister(RegisterDTO registerDTO) {

        User user = new User(registerDTO);

        Wallet wallet = new Wallet();

        cpfValidator.assertValid(user.getCpfCnpj());

        if (cpfValidator.isEligible(user.getCpfCnpj())) {

            String encodedPassword = passwordEncoder.encode(user.getPassword());

            user.setPassword(encodedPassword);

            wallet.setBalance(BigDecimal.valueOf(500.0));

            wallet.setUser(user);

            user.setWallet(wallet);

            return user;

        }

        throw new InvalidCPFException("sex");

    }

    public User validateSellerPropertiesBeforeRegister (RegisterDTO registerDTO) {

        User user = new User(registerDTO);

        Wallet wallet = new Wallet();

        cnpjValidator.assertValid(user.getCpfCnpj());

        if (cnpjValidator.isEligible(user.getCpfCnpj())) {

            String encodedPassword = passwordEncoder.encode(user.getPassword());

            user.setPassword(encodedPassword);

            wallet.setBalance(BigDecimal.valueOf(0.0));

            wallet.setUser(user);

            user.setWallet(wallet);

            return user;

        }

        throw new InvalidCNPJException("sex");

    }

    public void setupUserRolesAndSave (User user) {

        if (cpfValidator.isEligible(user.getCpfCnpj())) {

            roleRepository.findById(1L).ifPresent(

                    searchForRoles -> roleRepository.insertRole(user.getId(), searchForRoles.getId())

            );

        }

        else if (cnpjValidator.isEligible(user.getCpfCnpj())) {

            roleRepository.findById(2L).ifPresent(

                    searchForRoles -> roleRepository.insertRole(user.getId(), searchForRoles.getId())

            );

        }

    }

}
