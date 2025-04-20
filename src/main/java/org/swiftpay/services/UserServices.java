package org.swiftpay.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
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

    private final AsaasService asaasService;

    private final WalletService walletService;

    @Transactional
    public void registerAsClient (RegisterDTO registerDTO) {

        var validatedClient = rolesService.validateClientPropertiesBeforeRegister(registerDTO);

        userRepository.save(validatedClient);

        CustomerResponseDTO customerResponseDTO = asaasService.registerCustomerInAsaas(registerDTO);

        asaasService.saveCustomerInTheDB(new SaveAsaasCustomerDTO(customerResponseDTO.id(), validatedClient));

        rolesService.setupUserRolesAndSave(validatedClient);

        mailService.setupConfirmationEmailLogic(validatedClient);

        walletService.save(validatedClient, customerResponseDTO.walletId());

    }

    @Transactional
    public void registerAsSeller (RegisterDTO registerDTO) {

        var validatedSeller = rolesService.validateSellerPropertiesBeforeRegister(registerDTO);

        userRepository.save(validatedSeller);

        CustomerResponseDTO customerResponseDTO = asaasService.registerCustomerInAsaas(registerDTO);

        asaasService.saveCustomerInTheDB(new SaveAsaasCustomerDTO(customerResponseDTO.id(), validatedSeller));

        rolesService.setupUserRolesAndSave(validatedSeller);

        mailService.setupConfirmationEmailLogic(validatedSeller);

        walletService.save(validatedSeller, customerResponseDTO.walletId());

    }

    public String login (LoginDTO loginDTO) {

        return authService.validateLoginPropertiesThenGenerateToken(loginDTO);

    }

    @Transactional
    public void reactivateUserAccount (ReactivateUserDTO reactivateUserDTO) {

        var searchForAccount = userRepository
                                .findByEmail(reactivateUserDTO.email())
                                .orElseThrow(() -> new UserNotFoundException("We were not able to find an account with that email!"));

        authService.checkIfUserIsAlreadyActive(searchForAccount.getActive());

        searchForAccount.activate();

        userRepository.save(searchForAccount);

        deleteRegisterRepository.deleteByUser_Id(searchForAccount.getId());

    }

    @Transactional
    public void disableUserAccount (HttpHeaders headers, Long id) {

        var searchForAccount = userRepository
                                .findById(id)
                                .orElseThrow(() -> new UserNotFoundException("We weren't able to find a user with this id: " + id));

        authService.compareIdFromTheSessionWithTheIdInTheUrl(headers, id);

        searchForAccount.deactivate();

        var addUserToRegister = setupDeletionRegister(searchForAccount);

        deleteRegisterRepository.save(addUserToRegister);

        userRepository.save(searchForAccount);

        mailService.setupDeletionEmailLogic(searchForAccount.getEmail());

    }

    public User findUserById (Long id) {

        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("We were not able to find a user with this id: " + id));

    }

    public User getProfileById (HttpHeaders headers, Long id) {

        var searchForAccount = userRepository
                                .findById(id)
                                .orElseThrow(() -> new UserNotFoundException("We were not able to find a user with this id: " + id));

        authService.compareIdFromTheSessionWithTheIdInTheUrl(headers, id);

        return searchForAccount;

    }

    public void activateUserAccount (String token) {

        Hashids hashids = new Hashids("SHA-256");

        long [] decodeId = hashids.decode(token);

        long id = decodeId[0];

        var searchForAccount = userRepository
                                .findById(id)
                                .orElseThrow(() -> new UserNotFoundException("We weren't able to find a user with this id: " + id));

        searchForAccount.activate();

        userRepository.save(searchForAccount);

    }

    @Transactional
    @Scheduled(cron = "0 30 12 * * ?")
    public void deleteInactiveAccounts () {

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
