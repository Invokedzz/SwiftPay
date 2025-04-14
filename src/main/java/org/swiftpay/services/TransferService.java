package org.swiftpay.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.exceptions.InvalidTypeOfPayerException;
import org.swiftpay.infrastructure.clients.NotificationClient;
import org.swiftpay.infrastructure.policies.TransferAuthorizationPolicy;
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

    private final TransferAuthorizationPolicy transferAuthorizationPolicy;

    private final TransferRepository transferRepository;

    private final WalletRepository walletRepository;

    private final NotificationService notificationService;

    @Transactional
    public void transferToSomeone (TransferDTO transferDTO) {

        var payer = userRepository.findById(transferDTO.payerId()).orElseThrow();

        validateUserRolesBeforeTransfer(payer);

        transferAuthorizationPolicy.authorize(transferDTO);

        sendNotificationAfterTransfer();

    }

    private void sendNotificationAfterTransfer () {

        notificationService.sendNotification();

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
