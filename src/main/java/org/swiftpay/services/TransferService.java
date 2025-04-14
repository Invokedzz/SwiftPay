package org.swiftpay.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.exceptions.APIErrorException;
import org.swiftpay.exceptions.InvalidTypeOfPayerException;
import org.swiftpay.exceptions.UserNotFoundException;
import org.swiftpay.model.User;
import org.swiftpay.repositories.TransferRepository;
import org.swiftpay.repositories.UserRepository;
import org.swiftpay.repositories.WalletRepository;

@Service
@RequiredArgsConstructor
public class TransferService {

    @Value("${asaas.api.key}")
    private String asaasKey;

    private final UserRepository userRepository;

    private final TransferRepository transferRepository;

    private final WalletRepository walletRepository;

    private final NotificationService notificationService;

    private final AuthorizationService authorizationService;

    @Transactional
    public void transferToSomeoneSandbox (TransferDTO transferDTO) {

        var payer = userRepository.findById(transferDTO.payerId())
                                  .orElseThrow(() -> new UserNotFoundException("Payer id not found!"));

        validateUserRolesBeforeTransfer(payer);

        authorizationService.validateTransferBody(transferDTO);

        validateTransference();

        sendNotificationAfterTransfer();

    }

    private void sendNotificationAfterTransfer () {

        notificationService.sendNotification();

    }

    private void validateTransference () {

        if (!authorizationService.validateTransfer()) {

            throw new APIErrorException("The API is not accepting transfers right now. Please, try again sometime.");

        }

    }

    private void validateUserRolesBeforeTransfer (User payer) {

        var payerAuthorities = payer.getAuthorities()
                              .stream()
                              .map(GrantedAuthority::getAuthority)
                              .toList();

        if (payerAuthorities.getFirst().equals("ROLE_SELLER")) {

            throw new InvalidTypeOfPayerException("You're not allowed to transfer as a seller!");

        }

    }

}
