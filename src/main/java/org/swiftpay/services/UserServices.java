package org.swiftpay.services;

import jakarta.transaction.Transactional;
import org.hashids.Hashids;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.*;
import org.swiftpay.exceptions.*;
import org.swiftpay.model.DeleteRegister;
import org.swiftpay.model.User;
import org.swiftpay.repositories.DeleteRegisterRepository;
import org.swiftpay.repositories.UserRepository;

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

    private final DeleteRegisterRepository deleteRegisterRepository;

    private final RolesService rolesService;

    private final AuthService authService;

    private final MailService mailService;

    public UserServices (UserRepository userRepository,
                         DeleteRegisterRepository deleteRegisterRepository,
                         RolesService rolesService,
                         AuthService authService,
                         MailService mailService) {

        this.userRepository = userRepository;

        this.deleteRegisterRepository = deleteRegisterRepository;

        this.rolesService = rolesService;

        this.authService = authService;

        this.mailService = mailService;

    }

    @Transactional
    public void registerAsClient (RegisterDTO registerDTO) {

        var validatedClient = rolesService.validateClientPropertiesBeforeRegister(registerDTO);

        userRepository.save(validatedClient);

        rolesService.setupUserRolesAndSave(validatedClient);

        mailService.setupConfirmationEmailLogic(validatedClient);

    }

    @Transactional
    public void registerAsSeller (RegisterDTO registerDTO) {

        var validatedSeller = rolesService.validateSellerPropertiesBeforeRegister(registerDTO);

        userRepository.save(validatedSeller);

        rolesService.setupUserRolesAndSave(validatedSeller);

        mailService.setupConfirmationEmailLogic(validatedSeller);

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

            authService.checkIfUserIsAlreadyActive(searchForAccount.getActive());

            searchForAccount.activate();

            userRepository.save(searchForAccount);

            deleteRegisterRepository.deleteByUser_Id(searchForAccount.getId());

            return;

        }

        throw new UserNotFoundException("We were not able to find an account with that email");

    }

    @Transactional
    public void disableUserAccount(HttpHeaders headers, Long id) {

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

    public User getProfileById (HttpHeaders headers, Long id) {

        var searchForAccount = userRepository.findById(id)
                                             .orElse(null);

        if (searchForAccount != null) {

            authService.compareIdFromTheSessionWithTheIdInTheUrl(headers, id);

            return searchForAccount;

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


}
