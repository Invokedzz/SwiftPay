package org.swiftpay.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.exceptions.APIErrorException;
import org.swiftpay.exceptions.InvalidTypeOfPayerException;
import org.swiftpay.exceptions.UserNotFoundException;
import org.swiftpay.model.Transfer;
import org.swiftpay.model.User;
import org.swiftpay.repositories.TransferRepository;
import org.swiftpay.repositories.UserRepository;
import org.swiftpay.repositories.WalletRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final UserRepository userRepository;

    private final TransferRepository transferRepository;

    private final WalletRepository walletRepository;

    private final NotificationService notificationService;

    private final AuthorizationService authorizationService;

    @Transactional
    public void transferToSomeoneSandbox (TransferDTO transferDTO) {

        var payer = userRepository.findById(transferDTO.payerId())
                                  .orElseThrow(() -> new UserNotFoundException("Payer id not found!"));

        var payee = userRepository.findById(transferDTO.payeeId())
                                  .orElseThrow(() -> new UserNotFoundException("Payee id not found!"));

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

        walletRepository.save(payer.getWallet());

        walletRepository.save(payee.getWallet());

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
