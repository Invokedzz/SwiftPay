package org.swiftpay.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hashids.Hashids;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.*;
import org.swiftpay.exceptions.*;
import org.swiftpay.model.User;
import org.swiftpay.repositories.UserRepository;

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

    private final DeleteRegisterService deleteRegisterService;

    private final AsaasService asaasService;

    private final RolesService rolesService;

    private final AuthService authService;

    private final MailService mailService;

    private final CustomerService customerService;

    private final WalletService walletService;

    @Transactional
    public void registerAsClient (RegisterDTO registerDTO) {

        var validatedClient = rolesService.validateClientPropertiesBeforeRegister(registerDTO);

        checkIfAnyOfTheseInformationAlreadyExist(validatedClient);

        userRepository.save(validatedClient);

        CustomerResponseDTO customerResponseDTO = asaasService.registerCustomerInAsaas(registerDTO);

        customerService.saveCustomerInTheDB(new SaveAsaasCustomerDTO(customerResponseDTO.id(), validatedClient));

        rolesService.setupUserRolesAndSave(validatedClient);

        mailService.setupConfirmationEmailLogic(validatedClient);

        walletService.save(validatedClient, customerResponseDTO.walletId());

    }

    @Transactional
    public void registerAsSeller (RegisterDTO registerDTO) {

        var validatedSeller = rolesService.validateSellerPropertiesBeforeRegister(registerDTO);

        checkIfAnyOfTheseInformationAlreadyExist(validatedSeller);

        userRepository.save(validatedSeller);

        CustomerResponseDTO customerResponseDTO = asaasService.registerCustomerInAsaas(registerDTO);

        customerService.saveCustomerInTheDB(new SaveAsaasCustomerDTO(customerResponseDTO.id(), validatedSeller));

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

        deleteRegisterService.deleteUserFromRegister(searchForAccount.getId());

    }

    @Transactional
    public void disableUserAccount (HttpHeaders headers, Long id) {

        var searchForAccount = userRepository
                                .findById(id)
                                .orElseThrow(() -> new UserNotFoundException("We weren't able to find a user with this id: " + id));

        authService.compareIdFromTheSessionWithTheIdInTheUrl(headers, id);

        searchForAccount.deactivate();

        var setupUserToEnterInTheRegister = deleteRegisterService.setupDeletionRegister(searchForAccount);

        deleteRegisterService.saveUserIntoRegister(setupUserToEnterInTheRegister);

        userRepository.save(searchForAccount);

        mailService.setupDeletionEmailLogic(searchForAccount.getEmail());

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

    public User findUserByWalletId (String walletId) {

        return userRepository
                .findByWalletAsaasWalletIdEqualsIgnoreCase(walletId)
                .orElseThrow(() -> new UserNotFoundException("We weren't able to find a user with this id: " + walletId));

    }

    public User findUserById (Long id) {

        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("We were not able to find a user with this id: " + id));

    }

    private void checkIfAnyOfTheseInformationAlreadyExist (User user) {

        checkIfEmailAlreadyExists(user.getEmail());

        checkIfCPFCNPJAlreadyExists(user.getCpfCnpj());

        checkIfUsernameAlreadyExists(user.getUsername());

    }

    private void checkIfEmailAlreadyExists (String email) {

        var searchForEmail = userRepository.findByEmail(email);

        if (searchForEmail.isPresent()) {

            throw new InvalidEmailFormatException("Email already exists!");

        }

    }

    private void checkIfUsernameAlreadyExists (String username) {

        var searchForUsername = userRepository.findByUsernameEqualsIgnoreCase(username);

        if (searchForUsername.isPresent()) {

            throw new UsernameAlreadyExistsException("Username already exists!");

        }

    }

    private void checkIfCPFCNPJAlreadyExists (String cpfCnpj) {

        var searchForCpfCnpj = userRepository.findByCpfCnpj(cpfCnpj);

        if (searchForCpfCnpj.isPresent()) {

            throw new CPFCNPJAlreadyExistsException("CPF/CNPJ already exists!");

        }

    }

}
