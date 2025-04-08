package org.swiftpay.services;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.LoginDTO;
import org.swiftpay.dtos.ReactivateUserDTO;
import org.swiftpay.dtos.RegisterDTO;
import org.swiftpay.dtos.ViewAllUsersDTO;
import org.swiftpay.exceptions.ForbiddenAccessException;
import org.swiftpay.exceptions.UserNotFoundException;
import org.swiftpay.model.DeleteRegister;
import org.swiftpay.model.User;
import org.swiftpay.model.Wallet;
import org.swiftpay.repositories.DeleteRegisterRepository;
import org.swiftpay.repositories.RoleRepository;
import org.swiftpay.repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServices {

    /*

        Adjusts I need to make:

        validate the user id in the "disableUserAccount" method, then create permissions using Spring Security
        After that, I need to set up the email sender for functions like "reactivate and disable"

    */

    private final UserRepository userRepository;

    private final CPFValidator cpfValidator;

    private final CNPJValidator cnpjValidator;

    private final PasswordEncoder passwordEncoder;

    private final DeleteRegisterRepository deleteRegisterRepository;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    private final TokenAuthService tokenAuthService;

    public UserServices (UserRepository userRepository,
                         CPFValidator cpfValidator,
                         CNPJValidator cnpjValidator,
                         PasswordEncoder passwordEncoder,
                         DeleteRegisterRepository deleteRegisterRepository,
                         RoleRepository roleRepository,
                         AuthenticationManager authenticationManager,
                         TokenAuthService tokenAuthService) {

        this.userRepository = userRepository;

        this.cpfValidator = cpfValidator;

        this.cnpjValidator = cnpjValidator;

        this.passwordEncoder = passwordEncoder;

        this.deleteRegisterRepository = deleteRegisterRepository;

        this.roleRepository = roleRepository;

        this.authenticationManager = authenticationManager;

        this.tokenAuthService = tokenAuthService;

    }

    @Transactional
    public void registerAsClient (RegisterDTO registerDTO) {

        validateClientPropertiesBeforeRegisterThenSave(registerDTO);

    }

    @Transactional
    public void registerAsSeller (RegisterDTO registerDTO) {

        validateSellerPropertiesBeforeRegisterThenSave(registerDTO);

    }

    public String login (LoginDTO loginDTO) {

        return validateLoginPropertiesThenGenerateToken(loginDTO);

    }

    public Set <ViewAllUsersDTO> getUsers () {

        return userRepository.findAllByActive(true).stream()
                .map(ViewAllUsersDTO::new)
                .collect(Collectors.toSet());

    }

    @Transactional
    public void reactivateUserAccount (ReactivateUserDTO reactivateUserDTO) {

        var searchForAccount = userRepository.findByEmail(reactivateUserDTO.email())
                                            .orElse(null);

        if (searchForAccount != null) {

            searchForAccount.activate();

            userRepository.save(searchForAccount);

            deleteRegisterRepository.deleteByUser_Id(searchForAccount.getId());

            return;

        }

        throw new UserNotFoundException("We were not able to find an account with that email");

    }

    @Transactional
    public void disableUserAccount (HttpHeaders headers, Long id) {

        var searchForAccount = userRepository.findById(id)
                                             .orElse(null);

        if (searchForAccount != null) {

            compareIdFromTheSessionWithTheIdInTheUrl(headers, searchForAccount.getId());

            searchForAccount.deactivate();

            var addUserToRegister = setupDeletionRegister(searchForAccount);

            deleteRegisterRepository.save(addUserToRegister);

            userRepository.save(searchForAccount);

            return;

        }

        throw new UserNotFoundException("We weren't able to find a user with this id: " + id);

    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    protected void deleteInactiveAccounts () {

        var allDeleteRegisters = deleteRegisterRepository.findAll();

        for (DeleteRegister deleteRegister : allDeleteRegisters) {

            if (checkDaysBeforeAccountDeletion(deleteRegister.getDeleteDate()) > 0) {

                deleteRegisterRepository.delete(deleteRegister);

            }

        }

    }

    private Long checkDaysBeforeAccountDeletion (LocalDate date) {

        return ChronoUnit.DAYS.between(LocalDate.now(), date);

    }

    private DeleteRegister setupDeletionRegister (User user) {

        DeleteRegister deleteRegister = new DeleteRegister();

        deleteRegister.setUser(user);

        deleteRegister.setDeleteDate(LocalDate.now());

        return deleteRegister;

    }

    private String validateLoginPropertiesThenGenerateToken (LoginDTO loginDTO) {

        var searchForUser = new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password());

        var authentication = authenticationManager.authenticate(searchForUser);

        return tokenAuthService.generateToken((User) authentication.getPrincipal());

    }

    private void validateClientPropertiesBeforeRegisterThenSave (RegisterDTO registerDTO) {

        User user = new User(registerDTO);

        Wallet wallet = new Wallet();

        cpfValidator.assertValid(user.getCpfCnpj());

        if (cpfValidator.isEligible(user.getCpfCnpj())) {

            String encodedPassword = passwordEncoder.encode(user.getPassword());

            user.setPassword(encodedPassword);

            wallet.setBalance(BigDecimal.valueOf(500.0));

            wallet.setUser(user);

            user.setWallet(wallet);

            userRepository.save(user);

            setupUserRolesAndSave(user);

        }

    }

    private void validateSellerPropertiesBeforeRegisterThenSave (RegisterDTO registerDTO) {

        User user = new User(registerDTO);

        Wallet wallet = new Wallet();

        cnpjValidator.assertValid(user.getCpfCnpj());

        if (cnpjValidator.isEligible(user.getCpfCnpj())) {

            String encodedPassword = passwordEncoder.encode(user.getPassword());

            user.setPassword(encodedPassword);

            wallet.setBalance(BigDecimal.valueOf(0.0));

            wallet.setUser(user);

            user.setWallet(wallet);

            userRepository.save(user);

            setupUserRolesAndSave(user);

        }

    }

    private void setupUserRolesAndSave (User user) {

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

    private void compareIdFromTheSessionWithTheIdInTheUrl (HttpHeaders headers, Long sentId) {

        Long sessionId = tokenAuthService.findSessionId(headers);

        if (!sessionId.equals(sentId)) {

            throw new ForbiddenAccessException("You are not allowed to access this session");

        }

    }

}
