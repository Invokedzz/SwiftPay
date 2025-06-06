package org.swiftpay.services;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.RegisterDTO;
import org.swiftpay.exceptions.InvalidCNPJException;
import org.swiftpay.exceptions.InvalidCPFException;
import org.swiftpay.model.User;
import org.swiftpay.repositories.RoleRepository;

@Service
@RequiredArgsConstructor
public class RolesService {

    private final RoleRepository roleRepository;

    private final CPFValidator cpfValidator;

    private final CNPJValidator cnpjValidator;

    private final PasswordEncoder passwordEncoder;

    public User validateClientPropertiesBeforeRegister (RegisterDTO registerDTO) {

        User user = new User(registerDTO);

        cpfValidator.assertValid(user.getCpfCnpj());

        if (cpfValidator.isEligible(user.getCpfCnpj())) {

            String encodedPassword = passwordEncoder.encode(user.getPassword());

            user.setPassword(encodedPassword);

            return user;

        }

        throw new InvalidCPFException("Invalid CPF format. Please, try again.");

    }

    public User validateSellerPropertiesBeforeRegister (RegisterDTO registerDTO) {

        User user = new User(registerDTO);

        cnpjValidator.assertValid(user.getCpfCnpj());

        if (cnpjValidator.isEligible(user.getCpfCnpj())) {

            String encodedPassword = passwordEncoder.encode(user.getPassword());

            user.setPassword(encodedPassword);

            return user;

        }

        throw new InvalidCNPJException("Invalid CNPJ format. Please, try again.");

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
