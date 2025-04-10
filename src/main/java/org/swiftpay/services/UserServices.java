package org.swiftpay.services;

import jakarta.transaction.Transactional;
import org.hashids.Hashids;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.swiftpay.configuration.StellaConfig;
import org.swiftpay.dtos.*;
import org.swiftpay.exceptions.*;
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

        validate the user id in the "disableUserAccount" method, then create permissions using Spring Security -> Done!
        After that, I need to set up the email sender for functions like "reactivate and disable"

        Ps: What If I Implement the email sender only to register users account? :D

        Try to fix login method, and only permit users where the active is true -> Done!

    */

    private final UserRepository userRepository;

    private final StellaConfig stellaConfig;

    private final PasswordEncoder passwordEncoder;

    private final DeleteRegisterRepository deleteRegisterRepository;

    private final RoleRepository roleRepository;

    private final AuthService authService;

    private final MailService mailService;

    public UserServices (UserRepository userRepository,
                         StellaConfig stellaConfig,
                         PasswordEncoder passwordEncoder,
                         DeleteRegisterRepository deleteRegisterRepository,
                         RoleRepository roleRepository,
                         AuthService authService,
                         MailService mailService) {

        this.userRepository = userRepository;

        this.stellaConfig = stellaConfig;

        this.passwordEncoder = passwordEncoder;

        this.deleteRegisterRepository = deleteRegisterRepository;

        this.roleRepository = roleRepository;

        this.authService = authService;

        this.mailService = mailService;

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

        return authService.validateLoginPropertiesThenGenerateToken(loginDTO);

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

            checkIfUserIsAlreadyActive(searchForAccount.getActive());

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

            authService.compareIdFromTheSessionWithTheIdInTheUrl(headers, id);

            searchForAccount.deactivate();

            var addUserToRegister = setupDeletionRegister(searchForAccount);

            deleteRegisterRepository.save(addUserToRegister);

            userRepository.save(searchForAccount);

            mailService.setupDeletionEmailLogic(searchForAccount.getEmail());

            return;

        }

        throw new UserNotFoundException("We weren't able to find a user with this id: " + id);

    }

    public void activateUserAccount (String token) {

        Hashids hashids = new Hashids("SHA-256");

        long [] decodeId = hashids.decode(token);

        long id = decodeId[0];

        var searchForAccount = userRepository.findById(id)
                                             .orElse(null);

        if (searchForAccount != null) {

            searchForAccount.activate();

            userRepository.save(searchForAccount);

            return;

        }

        throw new UserNotFoundException("We weren't able to find a user with this id: " + id);

    }

    @Transactional
    @Scheduled(cron = "0 30 12 * * ?")
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

    private void validateClientPropertiesBeforeRegisterThenSave (RegisterDTO registerDTO) {

        User user = new User(registerDTO);

        Wallet wallet = new Wallet();

        stellaConfig.cpfValidator().assertValid(user.getCpfCnpj());

        if (stellaConfig.cpfValidator().isEligible(user.getCpfCnpj())) {

            String encodedPassword = passwordEncoder.encode(user.getPassword());

            user.setPassword(encodedPassword);

            wallet.setBalance(BigDecimal.valueOf(500.0));

            wallet.setUser(user);

            user.setWallet(wallet);

            userRepository.save(user);

            setupUserRolesAndSave(user);

            mailService.setupConfirmationEmailLogic(user);

        }

    }

    private void validateSellerPropertiesBeforeRegisterThenSave (RegisterDTO registerDTO) {

        User user = new User(registerDTO);

        Wallet wallet = new Wallet();

        stellaConfig.cnpjValidator().assertValid(user.getCpfCnpj());

        if (stellaConfig.cnpjValidator().isEligible(user.getCpfCnpj())) {

            String encodedPassword = passwordEncoder.encode(user.getPassword());

            user.setPassword(encodedPassword);

            wallet.setBalance(BigDecimal.valueOf(0.0));

            wallet.setUser(user);

            user.setWallet(wallet);

            userRepository.save(user);

            setupUserRolesAndSave(user);

            mailService.setupConfirmationEmailLogic(user);

        }

    }

    private void setupUserRolesAndSave (User user) {

        if (stellaConfig.cpfValidator().isEligible(user.getCpfCnpj())) {

            roleRepository.findById(1L).ifPresent(

                    searchForRoles -> roleRepository.insertRole(user.getId(), searchForRoles.getId())

            );

        }

        else if (stellaConfig.cnpjValidator().isEligible(user.getCpfCnpj())) {

            roleRepository.findById(2L).ifPresent(

                    searchForRoles -> roleRepository.insertRole(user.getId(), searchForRoles.getId())

            );

        }

    }

    private void checkIfUserIsAlreadyActive (Boolean isActive) {

        if (isActive) {

            throw new AlreadyActiveException("This user is already active!");

        }

    }

}
