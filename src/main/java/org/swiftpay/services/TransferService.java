package org.swiftpay.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.PaymentRequestDTO;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.dtos.PaymentResponseDTO;
import org.swiftpay.exceptions.APIErrorException;
import org.swiftpay.exceptions.InvalidTypeOfPayerException;
import org.swiftpay.model.Transfer;
import org.swiftpay.model.User;
import org.swiftpay.repositories.TransferRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final UserServices userServices;

    private final TransferRepository transferRepository;

    private final WalletService walletService;

    private final NotificationService notificationService;

    private final AuthorizationService authorizationService;

    private final AsaasService pixService;

    private final AuthService authService;

    @Transactional
    public void transferToSomeone (TransferDTO transferDTO) {}

    @Transactional
    public void transferToSomeoneSandbox (HttpHeaders headers, TransferDTO transferDTO) {

        var payer = userServices.findUserById(transferDTO.payerId());

        var payee = userServices.findUserById(transferDTO.payeeId());

        authService.compareIdFromTheSessionWithTheIdInTheUrl(headers, payer.getId());

        validateUserRolesBeforeTransfer(payer);

        authorizationService.validateTransferBody(transferDTO);

        authorizationService.compareValueAndBalance(payer.getWallet().getBalance(), transferDTO.value());

        transference(payer, payee, transferDTO.value());

        validateTransference();

        saveTransference(transferDTO, payer, payee);

        sendNotificationAfterTransfer();

    }

    private void transference (User payer, User payee, BigDecimal value) {

        payer.getWallet().setBalance(payer.getWallet().getBalance().subtract(value));

        payee.getWallet().setBalance(payee.getWallet().getBalance().add(value));

        walletService.save(payer.getWallet());

        walletService.save(payee.getWallet());

    }

    private void saveTransference (TransferDTO transferDTO, User payer, User payee) {

        transferRepository.save(new Transfer(transferDTO, LocalDate.now(), payer, payee));

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
