package org.swiftpay.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.model.Transfer;
import org.swiftpay.model.User;
import org.swiftpay.repositories.TransferRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SandboxTransferService {

    private final UserServices userServices;

    private final TransferRepository transferRepository;

    private final WalletService walletService;

    private final NotificationService notificationService;

    private final AuthorizationService authorizationService;

    private final AuthService authService;

    @Transactional
    public void transferToSomeoneSandbox (HttpHeaders headers, TransferDTO transferDTO) {

        var payer = userServices.findUserById(transferDTO.payerId());

        var payee = userServices.findUserById(transferDTO.payeeId());

        authService.compareIdFromTheSessionWithTheIdInTheUrl(headers, payer.getId());

        authorizationService.checkIfPayerIsASeller(payer);

        authorizationService.validateTransferBodySandboxVer(transferDTO);

        authorizationService.compareValueAndBalance(payer.getWallet().getBalance(), transferDTO.value());

        if (authorizationService.validateTransfer()) {

            transference(payer, payee, transferDTO.value());

            saveTransference(transferDTO, payer, payee);

            sendNotificationAfterTransfer();

        }

    }

    private void transference (User payer, User payee, BigDecimal value) {

        payer.getWallet().setBalance(payer.getWallet().getBalance().subtract(value));

        payee.getWallet().setBalance(payee.getWallet().getBalance().add(value));

        walletService.saveSandboxWallet(payer.getWallet());

        walletService.saveSandboxWallet(payee.getWallet());

    }

    private void saveTransference (TransferDTO transferDTO, User payer, User payee) {

        transferRepository.save(new Transfer(transferDTO, LocalDate.now(), payer, payee));

    }

    private void sendNotificationAfterTransfer () {

        notificationService.sendNotification();

    }

}
